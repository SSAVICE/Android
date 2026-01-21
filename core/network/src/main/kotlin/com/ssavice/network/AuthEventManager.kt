package com.ssavice.network

interface AuthEventManager {
    suspend fun emit(event: AuthEvent)
}

sealed class AuthEvent {
    object Unauthorized : AuthEvent() // 401 & 리프레시 실패
    object Forbidden : AuthEvent()    // 403
}
