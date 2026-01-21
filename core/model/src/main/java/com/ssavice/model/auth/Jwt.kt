package com.ssavice.model.auth

import com.ssavice.model.TimeStamp
import kotlinx.serialization.Serializable

@Serializable
data class Jwt(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: TimeStamp,
) {
    fun isExpired(): Boolean = System.currentTimeMillis() >= accessTokenExpiresAt.timeInMillis
}
