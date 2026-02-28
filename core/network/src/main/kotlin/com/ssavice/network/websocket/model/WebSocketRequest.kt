package com.ssavice.network.websocket.model

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketRequest (
    val messageType: String,
    val roomType: String,
    val roomId: String,
    val receiver: Long,
    val message: String,
    val serviceId: Long,
    val readMsgId: Long,
)
