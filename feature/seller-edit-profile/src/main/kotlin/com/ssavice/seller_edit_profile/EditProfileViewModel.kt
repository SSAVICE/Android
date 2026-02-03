package com.ssavice.edit_profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.RegionInfo
import com.ssavice.model.seller.SellerProfileUpdateForm
import com.ssavice.seller_edit_profile.AddressFormState
import com.ssavice.seller_edit_profile.AddressState
import com.ssavice.seller_edit_profile.EditProfileForm
import com.ssavice.seller_edit_profile.EditProfileState
import com.ssavice.seller_edit_profile.ProfileState
import com.ssavice.seller_edit_profile.navigation.EditProfileRouteContract
import com.ssavice.ui.model.AndroidResizableImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FormWithProfileImage(
    val form: EditProfileForm,
    val profileImageUrl: String,
)

@HiltViewModel
class EditProfileViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val sellerInfoRepository: SellerInfoRepository,
        @ApplicationContext private val context: Context,
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

        fun initAddressState() {
            _uiState.update {
                it.copy(addressState = AddressFormState.Fetching)
            }

            viewModelScope.launch(Dispatchers.IO) {
                sellerInfoRepository.getSellerAddress().fold(
                    onSuccess = { address ->
                        _uiState.update {
                            it.copy(
                                form =
                                    it.form.copy(
                                        address =
                                            AddressState(
                                                address = address.regionInfo.address,
                                                latitude = address.regionInfo.latitude,
                                                longitude = address.regionInfo.longitude,
                                                regionCode = address.regionInfo.regionCode,
                                                postCode = address.regionInfo.postCode,
                                            ),
                                        detailAddress = address.regionInfo.detailAddress,
                                    ),
                            )
                        }
                    },
                    onFailure = {
                        Log.e(LOG, "initAddressState: ", it)
                    },
                )
                _uiState.update {
                    it.copy(addressState = AddressFormState.Idle)
                }
            }
        }

        private fun getProfileFromSavedStateHandle(): FormWithProfileImage? {
            val name = savedStateHandle.get<String>(EditProfileRouteContract.NAME)
            val description = savedStateHandle.get<String>(EditProfileRouteContract.DESCRIPTION)
            val detail = savedStateHandle.get<String>(EditProfileRouteContract.DETAIL)
            val phoneNumber = savedStateHandle.get<String>(EditProfileRouteContract.PHONE_NUMBER)
            val thumbnail = savedStateHandle.get<String>(EditProfileRouteContract.IMAGE_URL)

            if (name == null || description == null || phoneNumber == null || thumbnail == null) {
                return null
            }
            if (name.isEmpty() && description.isEmpty() && phoneNumber.isEmpty() && thumbnail.isEmpty()) {
                return null
            }

            return FormWithProfileImage(
                form =
                    EditProfileForm(
                        name = name,
                        description = description,
                        detail = detail ?: "",
                        phoneNumber = phoneNumber.filter { it.isDigit() },
                    ),
                profileImageUrl = thumbnail,
            )
        }

        private suspend fun getProfileRemote(): FormWithProfileImage {
            sellerInfoRepository
                .getMySellerInformation()
                .last()
                .let { result ->
                    return FormWithProfileImage(
                        form =
                            EditProfileForm(
                                name = result.companyName,
                                description = result.description,
                                detail = result.detail,
                                phoneNumber = result.phoneNumber.filter { it.isDigit() },
                            ),
                        profileImageUrl = result.imageUrl,
                    )
                }
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

        private fun checkDescriptionValid(): Boolean {
            if (_uiState.value.form.description
                    .isBlank()
            ) {
                _uiState.update {
                    it.copy(form = it.form.copy(descriptionErrorMessage = "판매자 소개를 입력해주세요"))
                }
                return false
            } else {
                _uiState.update { it.copy(form = it.form.copy(descriptionErrorMessage = null)) }
                return true
            }
        }

        private fun checkDetailValid(): Boolean {
            if (_uiState.value.form.detail
                    .isBlank()
            ) {
                _uiState.update {
                    it.copy(form = it.form.copy(detailErrorMessage = "상세 설명을 입력해주세요"))
                }
                return false
            } else {
                _uiState.update { it.copy(form = it.form.copy(detailErrorMessage = null)) }
                return true
            }
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

        private fun isStateModifiable(state: ProfileState): Boolean = (state is ProfileState.Error || state is ProfileState.Idle)

        private fun isStateModifiable(state: AddressFormState): Boolean =
            (state !is AddressFormState.Initial && state !is AddressFormState.Fetching)

        fun onUpdateButtonClick() {
            if (!isStateModifiable(_uiState.value.profileUpdateState)) return

            var valid = checkNameValid()
            valid = checkDescriptionValid() && valid
            valid = checkDetailValid() && valid
            valid = checkPhoneNumberValid() && valid
            if (valid) {
                updateProfile(
                    _uiState.value.form.name,
                    _uiState.value.form.description,
                    _uiState.value.form.detail,
                    _uiState.value.form.phoneNumber,
                )
            }
        }

        private fun updateProfile(
            name: String,
            description: String,
            detail: String,
            phoneNumber: String,
        ) {
            _uiState.update {
                it.copy(profileUpdateState = ProfileState.Updating)
            }

            viewModelScope.launch(Dispatchers.IO) {
                sellerInfoRepository
                    .updateSellerProfile(
                        SellerProfileUpdateForm(
                            sellerName = name,
                            phoneNumber = phoneNumber,
                            description = description,
                            detail = detail,
                            region =
                                RegionInfo(
                                    latitude = _uiState.value.form.address.latitude,
                                    longitude = _uiState.value.form.address.longitude,
                                    address = _uiState.value.form.address.address,
                                    detailAddress = _uiState.value.form.detailAddress,
                                    postCode = _uiState.value.form.address.postCode,
                                    regionCode = _uiState.value.form.address.regionCode,
                                ),
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

        fun onDescriptionChange(description: String) {
            if (!isStateModifiable(_uiState.value.profileUpdateState)) return
            _uiState.update {
                it.copy(form = it.form.copy(description = description))
            }
        }

        fun onDetailChange(detail: String) {
            if (!isStateModifiable(_uiState.value.profileUpdateState)) return
            _uiState.update {
                it.copy(form = it.form.copy(detail = detail))
            }
        }

        fun onPhoneNumberChange(phoneNumber: String) {
            if (!isStateModifiable(_uiState.value.profileUpdateState)) return
            _uiState.update {
                it.copy(form = it.form.copy(phoneNumber = phoneNumber))
            }
        }

        fun onAddressUpdate(addressState: AddressState) {
            _uiState.update {
                it.copy(form = it.form.copy(address = addressState))
            }
        }

        fun onDetailAddressChange(detailAddress: String) {
            if (!isStateModifiable(_uiState.value.addressState)) return
            _uiState.update {
                it.copy(form = it.form.copy(detailAddress = detailAddress))
            }
        }

        fun onUserProfileImageSelected(uri: Uri) {
            _uiState.update {
                it.copy(
                    imageSelectedUri = uri,
                    imageUpdateState = ProfileState.Updating,
                    imageUploadProgress = ImageUploadProgress.Preprocessing,
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                val inputStream = context.contentResolver.openInputStream(uri)
                val byteArray = inputStream?.readBytes()

                if (byteArray == null) {
                    return@launch
                }
                val image =
                    AndroidResizableImage(byteArray, "").compressToTargetSize(
                        1024 * 1024 * 3,
                    )
                sellerInfoRepository.updateSellerProfileImage(image).collect { progress ->
                    if (progress is ImageUploadProgress.Done) {
                        _uiState.update {
                            it.copy(
                                imageUploadProgress = progress,
                                imageUpdateState = ProfileState.Idle,
                            )
                        }
                        Log.d(LOG, "onUserProfileImageSelected: ${_uiState.value}")
                    }
                    if (progress is ImageUploadProgress.Error) {
                        _uiState.update {
                            it.copy(
                                imageUploadProgress = progress,
                                imageUpdateState = ProfileState.Error("이미지 업로드에 실패했습니다"),
                            )
                        }
                        Log.e(LOG, "onUserProfileImageSelected: ", progress.throwable)
                    } else {
                        _uiState.update {
                            it.copy(imageUploadProgress = progress)
                        }
                    }
                }
            }
        }

        companion object {
            const val LOG = "EditSellerProfileViewModel"
        }
    }
