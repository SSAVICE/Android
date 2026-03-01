package com.ssavice.network.model.company

import kotlinx.serialization.Serializable

@Serializable
data class ValidateBusinessDTO(
    val name: String,
    val startDate: String,
    val businessNumber: String,
    val businessName: String,
)

@Serializable
data class ValidateBusinessResponseDTO(
    val verifyToken: String,
)
