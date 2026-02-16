package com.ssavice.network.model.user

import com.ssavice.model.user.UserProfileUpdateForm
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileDTO(
    val email: String,
    val name: String,
    val phoneNumber: String,
) {
    companion object {
        fun fromModel(model: UserProfileUpdateForm): UpdateUserProfileDTO =
            UpdateUserProfileDTO(
                email = model.email,
                name = model.name,
                phoneNumber = model.phoneNumber,
            )
    }
}

@Serializable
data class UpdateUserProfileResponseDTO(
    val address: String?,
    val detailAddress: String?,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val postCode: String?,
)
