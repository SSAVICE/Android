package com.ssavice.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ValidateBusinessDTO (
    val name: String,
    val startDate: String,
    val businessNumber: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ValidateBusinessResponseDTO(
    val verifyToken: String
)
