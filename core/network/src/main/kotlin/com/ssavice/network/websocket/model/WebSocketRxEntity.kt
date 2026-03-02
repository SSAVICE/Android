package com.ssavice.network.websocket.model

import kotlinx.serialization.Serializable

interface WebSocketChatMessage {
    val roomId: String
    val messageId: Long
    val senderId: Long
    val createdAt: Long
}

@Serializable
sealed interface WebSocketRxEntity {
    data class TextChat(
        val content: String,
        override val roomId: String,
        override val senderId: Long,
        override val createdAt: Long,
        override val messageId: Long,
    ) : WebSocketRxEntity,
        WebSocketChatMessage

    data class ServiceInfoChat(
        val serviceId: Long,
        override val roomId: String,
        override val senderId: Long,
        override val createdAt: Long,
        override val messageId: Long,
    ) : WebSocketRxEntity,
        WebSocketChatMessage

    object Ignore : WebSocketRxEntity
}
