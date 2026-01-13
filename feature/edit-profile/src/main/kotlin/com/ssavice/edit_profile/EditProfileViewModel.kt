package com.ssavice.edit_profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.edit_profile.navigation.EditProfileRouteContract
import com.ssavice.model.user.UserProfileUpdateForm
import dagger.hilt.android.lifecycle.HiltViewModel
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
class EditProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<EditProfileState>(
        EditProfileState(
            form = EditProfileForm(),
            profileImage = EditProfileImage.UrlImage(""),
            profileUpdateState = ProfileState.Initial,
            imageUpdateState = ProfileState.Initial
        )
    )

    val uiState = _uiState.asStateFlow()

    fun initUiState() {
        _uiState.update {
            it.copy(
                profileUpdateState = ProfileState.Fetching,
                imageUpdateState = ProfileState.Fetching
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            val fetchedProfileData: FormWithProfileImage
            val fromLocal = getProfileFromSavedStateHandle()
            fetchedProfileData = fromLocal ?: getProfileRemote()

            _uiState.update {
                it.copy(
                    form = fetchedProfileData.form,
                    profileImage = EditProfileImage.UrlImage(fetchedProfileData.profileImageUrl),
                    profileUpdateState = ProfileState.Idle,
                    imageUpdateState = ProfileState.Idle
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
        if(name.isEmpty() && email.isEmpty() && phoneNumber.isEmpty() && thumbnail.isEmpty()) {
            return null
        }

        return FormWithProfileImage(
            form = EditProfileForm(
                name = name,
                email = email,
                phoneNumber = phoneNumber,
            ),
            profileImageUrl = thumbnail
        )
    }

    private suspend fun getProfileRemote(): FormWithProfileImage {
        userInfoRepository.getUserProfile()
            .fold(
                onSuccess = { result ->
                    return FormWithProfileImage(
                        form = EditProfileForm(
                            name = result.name,
                            email = result.email,
                            phoneNumber = result.phoneNumber
                        ),
                        profileImageUrl = result.imageUrl
                    )
                },
                onFailure = {
                    Log.e(
                        LOG, "getProfileRemote: ", it
                    )
                    return FormWithProfileImage(
                        form = EditProfileForm(),
                        profileImageUrl = ""
                    )
                }
            )
    }

    private fun checkNameValid(): Boolean {
        if (_uiState.value.form.name.isBlank()) {
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
        (state !is ProfileState.Error && state !is ProfileState.Idle)

    fun onUpdateButtonClick() {
        if (!isStateModifiable(_uiState.value.profileUpdateState)) return

        var valid = checkNameValid()
        valid = checkEmailValid() && valid
        valid = checkPhoneNumberValid() && valid
        if (valid) {
            updateProfile(
                _uiState.value.form.name,
                _uiState.value.form.email,
                _uiState.value.form.phoneNumber
            )
        }
    }

    private fun updateProfile(name: String, email: String, phoneNumber: String) {
        _uiState.update {
            it.copy(profileUpdateState = ProfileState.Updating)
        }

        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository.updateUserProfile(
                UserProfileUpdateForm(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber
                )
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(form = it.form.copy(name = name))
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(form = it.form.copy(email = email))
        }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update {
            it.copy(form = it.form.copy(phoneNumber = phoneNumber))
        }
    }

    companion object {
        const val LOG = "EditProfileViewModel"
    }
}
