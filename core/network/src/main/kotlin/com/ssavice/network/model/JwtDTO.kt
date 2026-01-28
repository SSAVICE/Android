package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.TimeStamp
import com.ssavice.model.auth.Jwt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@SuppressLint("UnsafeOptInUsageError")
@Serializable
@JsonIgnoreUnknownKeys
data class JwtDTO(
    @SerialName("expiresIn")
    val expirationSecond: Long,
    val refreshToken: String,
    val accessToken: String,
) {
    fun toJwt(): Jwt =
        Jwt(
            accessTokenExpiresAt = TimeStamp(expirationSecond * 1000L + System.currentTimeMillis()),
            refreshToken = refreshToken,
            accessToken = accessToken,
        )
}
