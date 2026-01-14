package com.ssavice.edit_profile

import android.net.Uri
import com.ssavice.model.ImageUploadProgress

data class EditProfileState(
    val form: EditProfileForm,
    val profileImage: String,
    val profileUpdateState: ProfileState = ProfileState.Initial,
    val imageUpdateState: ProfileState = ProfileState.Initial,
    val imageUploadProgress: ImageUploadProgress = ImageUploadProgress.Waiting,
    val imageSelectedUri: Uri? = null,
)

data class EditProfileForm(
    val name: String = "",
    val nameErrorMessage: String? = null,
    val email: String = "",
    val emailErrorMessage: String? = null,
    val phoneNumber: String = "",
    val phoneNumberErrorMessage: String? = null,
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
