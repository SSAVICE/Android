package com.ssavice.network.websocket.model

sealed interface WebSocketRxEntity{
    data class NewChat(
        val roomId: Long,
        val senderId: Long,
        val content: String,
        val createdAt: Long,
        val type: String
    ): WebSocketTxEntity
}
