package com.ssavice.network.websocket.model

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketEvent<T : WebSocketRxEntity>(
    val type: String,
    val data: T,
)
