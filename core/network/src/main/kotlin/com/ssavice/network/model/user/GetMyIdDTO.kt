package com.ssavice.network.model.user

import kotlinx.serialization.Serializable

@Serializable
data class GetMyIdDTO(
    val id: Long,
)
