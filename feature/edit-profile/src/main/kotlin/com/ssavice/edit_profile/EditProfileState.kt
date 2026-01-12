package com.ssavice.edit_profile

import android.graphics.Bitmap

data class EditProfileState(
    val form: EditProfileForm,
    val profileImage: EditProfileImage,
    val profileUpdateState: ProfileUpdateState = ProfileUpdateState.Idle,
    val imageUpdateState: ImageUpdateState = ImageUpdateState.Idle
)

data class EditProfileForm(
    val name: String = "",
    val nameErrorMessage: String? = null,
    val email: String = "",
    val emailErrorMessage: String? = null,
    val phoneNumber: String = "",
    val phoneNumberErrorMessage: String? = null
)

sealed interface EditProfileImage {
    data class BitmapImage(
        val bitmap: Bitmap
    ): EditProfileImage
    data class UrlImage(
        val url: String
    ): EditProfileImage
}

sealed interface ProfileUpdateState {
    object Idle: ProfileUpdateState

    object Done: ProfileUpdateState

    object Updating: ProfileUpdateState

    data class Error(val message: String): ProfileUpdateState
}

sealed interface ImageUpdateState {
    object Idle: ImageUpdateState

    object Done: ImageUpdateState

    object Updating: ImageUpdateState

    data class Error(val message: String): ImageUpdateState
}
