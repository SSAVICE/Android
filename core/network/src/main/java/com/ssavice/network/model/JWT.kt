package com.ssavice.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class JWT(
    @SerialName("expiresIn")
    val expirationSecond: Long,
    val refreshToken: String,
    val accessToken: String,
) {
    fun isExpired(): Boolean = false
}
