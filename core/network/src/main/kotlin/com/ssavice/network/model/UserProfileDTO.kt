package com.ssavice.network.model

import com.ssavice.model.Date
import com.ssavice.model.user.UserProfile
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDTO(
    val address: String?,
    val createdAt: String,
    val detailAddress: String?,
    val email: String,
    val imageUrl: String,
    val name: String,
    val phoneNumber: String,
    val postCode: String?,
) {
    fun toModel(): UserProfile =
        UserProfile(
            address = address ?: "",
            createdAt = Date.parse(createdAt),
            detailAddress = detailAddress ?: "",
            email = email,
            imageUrl = imageUrl,
            name = name,
            phoneNumber = phoneNumber,
            postCode = postCode?.toIntOrNull() ?: 0,
        )
}
