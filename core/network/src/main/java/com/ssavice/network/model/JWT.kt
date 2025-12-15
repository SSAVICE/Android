package com.ssavice.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class JWT(
    val expirationTick: Long,
    val refreshToken: String,
    val accessToken: String,
) {
    fun isExpired(): Boolean = false
}
