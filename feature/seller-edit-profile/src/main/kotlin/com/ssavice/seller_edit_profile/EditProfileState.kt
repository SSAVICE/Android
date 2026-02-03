package com.ssavice.seller_edit_profile

import android.net.Uri
import com.ssavice.model.ImageUploadProgress

data class EditProfileState(
    val form: EditProfileForm,
    val profileImage: String,
    val profileUpdateState: ProfileState = ProfileState.Initial,
    val imageUpdateState: ProfileState = ProfileState.Initial,
    val addressState: AddressFormState = AddressFormState.Initial,
    val imageUploadProgress: ImageUploadProgress = ImageUploadProgress.Waiting,
    val imageSelectedUri: Uri? = null,
)

data class EditProfileForm(
    val name: String = "",
    val nameErrorMessage: String? = null,
    val description: String = "",
    val descriptionErrorMessage: String? = null,
    val detail: String = "",
    val detailErrorMessage: String? = null,
    val phoneNumber: String = "",
    val phoneNumberErrorMessage: String? = null,
    val detailAddress: String = "",
    val detailAddressErrorMessage: String? = null,
    val address: AddressState = AddressState(),
)

data class AddressState(
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val regionCode: String = "",
    val postCode: String = "",
)

sealed interface ProfileState {
    object Initial : ProfileState

    object Fetching : ProfileState

    object Idle : ProfileState

    object Done : ProfileState

    object Updating : ProfileState

    data class Error(
        val message: String,
    ) : ProfileState
}

sealed interface AddressFormState {
    object Initial : AddressFormState

    object Fetching : AddressFormState

    object Idle : AddressFormState
}
