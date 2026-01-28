package com.ssavice.network

import AuthErrorResponse
import ErrorResponse
import kotlinx.serialization.json.Json
import okhttp3.Response

internal fun Response.parseAuthError(): AuthErrorResponse? =
    try {
        val json = this.peekBody(Long.MAX_VALUE).string()
        Json.decodeFromString(json)
    } catch (e: Exception) {
        null
    }

internal fun Response.parseError(): ErrorResponse? =
    try {
        val json = this.peekBody(Long.MAX_VALUE).string()
        Json.decodeFromString(json)
    } catch (e: Exception) {
        null
    }
