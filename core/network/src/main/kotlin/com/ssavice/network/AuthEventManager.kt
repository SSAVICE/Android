package com.ssavice.network

import kotlinx.coroutines.flow.SharedFlow

interface AuthEventManager {
    suspend fun emit(event: AuthEvent)

    val event: SharedFlow<AuthEvent>
}

sealed interface AuthEvent {
    object Unauthorized : AuthEvent // 401 & 리프레시 실패

    object Forbidden : AuthEvent // 403

    object Initial : AuthEvent
}
