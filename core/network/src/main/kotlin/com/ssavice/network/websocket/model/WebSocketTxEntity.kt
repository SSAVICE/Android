package com.ssavice.network.websocket.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface WebSocketTxEntity {
    data class SendChat(
        val roomId: Long,
        val content: String,
        val type: String,
        val receiverId: Long,
        val messageType: String,
    ) : WebSocketTxEntity

    data class SendRead(
        val roomId: Long,
        val lastMessageId: Long
    ) : WebSocketTxEntity

    data class NewDM(
        val receiverId: Long,
        val content: String,
        val type: String,
    ) : WebSocketTxEntity
}
