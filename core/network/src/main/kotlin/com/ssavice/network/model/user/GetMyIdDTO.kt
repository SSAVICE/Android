package com.ssavice.network.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetMyIdDTO(
    @SerialName("accountId")
    val id: Long,
)
