package com.ssavice.model.user

import com.ssavice.model.Date

data class UserProfile(
    val imageUrl: String,
    val name: String,
    val createdAt: Date,
    val email: String,
    val phoneNumber: String,
    val postCode: Int,
    val address: String,
    val detailAddress: String,
)
