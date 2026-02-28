package com.ssavice.network.websocket.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface WebSocketRxEntity{
    data class SendChat(
        val roomId: Long,
        val senderId: Long,
        val content: String,
        val createdAt: Long,
        val type: String,
        val messageId: Long,
    ): WebSocketRxEntity
}
