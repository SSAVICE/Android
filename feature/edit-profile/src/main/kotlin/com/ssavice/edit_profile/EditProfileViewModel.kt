package com.ssavice.edit_profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.edit_profile.navigation.EditProfileRouteContract
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.user.UserProfileUpdateForm
import com.ssavice.ui.model.AndroidResizableImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FormWithProfileImage(
    val form: EditProfileForm,
    val profileImageUrl: String
)

@HiltViewModel
class EditProfileViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userInfoRepository: UserInfoRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            EditProfileState(
                form = EditProfileForm(),
                profileImage = "",
                profileUpdateState = ProfileState.Initial,
                imageUpdateState = ProfileState.Initial,
            ),
        )

    val uiState = _uiState.asStateFlow()

    fun initUiState() {
        _uiState.update {
            it.copy(
                profileUpdateState = ProfileState.Fetching,
                imageUpdateState = ProfileState.Fetching,
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val fetchedProfileData: FormWithProfileImage
            val fromLocal = getProfileFromSavedStateHandle()
            fetchedProfileData = fromLocal ?: getProfileRemote()

            _uiState.update {
                it.copy(
                    form = fetchedProfileData.form,
                    profileImage = fetchedProfileData.profileImageUrl,
                    profileUpdateState = ProfileState.Idle,
                    imageUpdateState = ProfileState.Idle,
                )
            }
        }
    }

    private fun getProfileFromSavedStateHandle(): FormWithProfileImage? {
        val name = savedStateHandle.get<String>(EditProfileRouteContract.NAME)
        val email = savedStateHandle.get<String>(EditProfileRouteContract.EMAIL)
        val phoneNumber = savedStateHandle.get<String>(EditProfileRouteContract.PHONE_NUMBER)
        val thumbnail = savedStateHandle.get<String>(EditProfileRouteContract.IMAGE_URL)

        if (name == null || email == null || phoneNumber == null || thumbnail == null) {
            return null
        }
        if (name.isEmpty() && email.isEmpty() && phoneNumber.isEmpty() && thumbnail.isEmpty()) {
            return null
        }

        return FormWithProfileImage(
            form =
                EditProfileForm(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber.filter { it.isDigit() },
                ),
            profileImageUrl = thumbnail,
        )
    }

    private suspend fun getProfileRemote(): FormWithProfileImage {
        userInfoRepository
            .getUserProfile()
            .fold(
                onSuccess = { result ->
                    return FormWithProfileImage(
                        form =
                            EditProfileForm(
                                name = result.name,
                                email = result.email,
                                phoneNumber = result.phoneNumber.filter { it.isDigit() },
                            ),
                        profileImageUrl = result.imageUrl,
                    )
                },
                onFailure = {
                    Log.e(
                        LOG,
                        "getProfileRemote: ",
                        it,
                    )
                    return FormWithProfileImage(
                        form = EditProfileForm(),
                        profileImageUrl = "",
                    )
                },
            )
    }

    private fun checkNameValid(): Boolean {
        if (_uiState.value.form.name
                .isBlank()
        ) {
            _uiState.update {
                it.copy(form = it.form.copy(nameErrorMessage = "이름을 입력해주세요"))
            }
            return false
        } else {
            _uiState.update { it.copy(form = it.form.copy(nameErrorMessage = null)) }
            return true
        }
    }

    private fun checkEmailValid(): Boolean {
        val email = _uiState.value.form.email
        if (email.isBlank()) {
            _uiState.update {
                it.copy(form = it.form.copy(emailErrorMessage = "이메일을 입력해주세요"))
            }
            return false
        }

        val emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        if (!email.matches(emailPattern)) {
            _uiState.update {
                it.copy(form = it.form.copy(emailErrorMessage = "올바르지 않은 이메일 형식입니다"))
            }
            return false
        }

        _uiState.update { it.copy(form = it.form.copy(emailErrorMessage = null)) }
        return true
    }

    private fun checkPhoneNumberValid(): Boolean {
        if (_uiState.value.form.phoneNumber.length < 10) {
            _uiState.update {
                it.copy(form = it.form.copy(phoneNumberErrorMessage = "올바른 전화번호를 입력해주세요"))
            }
            return false
        } else {
            _uiState.update { it.copy(form = it.form.copy(phoneNumberErrorMessage = null)) }
            return true
        }
    }

    private fun isStateModifiable(state: ProfileState): Boolean =
        (state is ProfileState.Error || state is ProfileState.Idle)

    fun onUpdateButtonClick() {
        if (!isStateModifiable(_uiState.value.profileUpdateState)) return

        var valid = checkNameValid()
        valid = checkEmailValid() && valid
        valid = checkPhoneNumberValid() && valid
        if (valid) {
            updateProfile(
                _uiState.value.form.name,
                _uiState.value.form.email,
                _uiState.value.form.phoneNumber,
            )
        }
    }

    private fun updateProfile(
        name: String,
        email: String,
        phoneNumber: String,
    ) {
        _uiState.update {
            it.copy(profileUpdateState = ProfileState.Updating)
        }

        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository
                .updateUserProfile(
                    UserProfileUpdateForm(
                        name = name,
                        email = email,
                        phoneNumber = phoneNumber,
                    ),
                ).fold(
                    onSuccess = {
                        _uiState.update {
                            Log.d(LOG, "updateProfile: $it")
                            it.copy(
                                profileUpdateState = ProfileState.Done,
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.update {
                            Log.e(LOG, "updateProfile: ", e)
                            it.copy(
                                profileUpdateState = ProfileState.Error("프로필 업데이트에 실패했습니다"),
                            )
                        }
                    },
                )
        }
    }

    fun onNameChange(name: String) {
        if (!isStateModifiable(_uiState.value.profileUpdateState)) return
        _uiState.update {
            it.copy(form = it.form.copy(name = name))
        }
    }

    fun onEmailChange(email: String) {
        if (!isStateModifiable(_uiState.value.profileUpdateState)) return
        _uiState.update {
            it.copy(form = it.form.copy(email = email))
        }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        if (!isStateModifiable(_uiState.value.profileUpdateState)) return
        _uiState.update {
            it.copy(form = it.form.copy(phoneNumber = phoneNumber))
        }
    }

    fun onUserProfileImageSelected(uri: Uri) {
        _uiState.update {
            it.copy(
                imageSelectedUri = uri,
                imageUpdateState = ProfileState.Updating,
                imageUploadProgress = ImageUploadProgress.Preprocessing
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val byteArray = inputStream?.readBytes()

            if(byteArray == null) {
                return@launch
            }
            val image = AndroidResizableImage(byteArray, "").compressToTargetSize(
                1024*1024*3
            )
            userInfoRepository.updateUserProfileImage(image).collect { progress ->
                if(progress is ImageUploadProgress.Done) {
                    _uiState.update {
                        it.copy(
                            imageUploadProgress = progress,
                            imageUpdateState = ProfileState.Idle
                        )
                    }
                    Log.d(LOG, "onUserProfileImageSelected: ${_uiState.value}")
                }
                if(progress is ImageUploadProgress.Error) {
                    _uiState.update {
                        it.copy(
                            imageUploadProgress = progress,
                            imageUpdateState = ProfileState.Error("이미지 업로드에 실패했습니다")
                        )
                    }
                    Log.e(LOG, "onUserProfileImageSelected: ", progress.throwable)
                }
                else{
                    _uiState.update {
                        it.copy(imageUploadProgress = progress)
                    }
                }
            }
        }
    }

    companion object {
        const val LOG = "EditProfileViewModel"
    }
}
