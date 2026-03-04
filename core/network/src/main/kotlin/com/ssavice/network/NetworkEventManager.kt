package com.ssavice.network

import kotlinx.coroutines.flow.SharedFlow

interface NetworkEventManager {
    fun tryEmit(event: NetworkEvent)

    suspend fun emit(event: NetworkEvent)

    val event: SharedFlow<NetworkEvent>
}

sealed interface NetworkEvent {
    object Unauthorized : NetworkEvent // 401 & 리프레시 실패

    object Forbidden : NetworkEvent // 403

    object Initial : NetworkEvent

    object NetworkUnavailable : NetworkEvent

    object ChatServerUnavailable : NetworkEvent
}
