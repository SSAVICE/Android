package com.ssavice.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class LoginDTO (
    @SerialName("token")
    val kakaoAccessToken: String
)
