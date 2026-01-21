package com.ssavice.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class CompanyVerifyToken(
    val token: String,
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()

        return currentTime > createdAt + EXPIRATION_TIME
    }

    companion object {
        const val EXPIRATION_TIME = 1000 * 60 * 10
    }
}
