package com.ssavice.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(
    @SerialName("token")
    val kakaoAccessToken: String,
    @SerialName("provider")
    val authProvider: String,
)
