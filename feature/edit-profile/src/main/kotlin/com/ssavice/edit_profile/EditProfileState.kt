package com.ssavice.edit_profile

import android.graphics.Bitmap

data class EditProfileState(
    val form: EditProfileForm,
    val profileImage: EditProfileImage,
    val profileUpdateState: ProfileState = ProfileState.Initial,
    val imageUpdateState: ProfileState = ProfileState.Initial,
)

data class EditProfileForm(
    val name: String = "",
    val nameErrorMessage: String? = null,
    val email: String = "",
    val emailErrorMessage: String? = null,
    val phoneNumber: String = "",
    val phoneNumberErrorMessage: String? = null,
)

sealed interface EditProfileImage {
    data class BitmapImage(
        val bitmap: Bitmap,
    ) : EditProfileImage

    data class UrlImage(
        val url: String,
    ) : EditProfileImage
}

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
