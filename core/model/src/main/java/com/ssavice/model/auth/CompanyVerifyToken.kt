package com.ssavice.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class CompanyVerifyToken(
    val token: String,
    val createdAt: Long
) {
    fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()

        return currentTime > createdAt + EXPIRATION_TIME
    }

    companion object {
        private const val EXPIRATION_TIME = 1000 * 60 * 10
    }
}
