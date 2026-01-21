package com.ssavice.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ValidateBusinessDTO(
    val name: String,
    val startDate: String,
    val businessNumber: String,
)

@Serializable
data class ValidateBusinessResponseDTO(
    val verifyToken: String,
)
