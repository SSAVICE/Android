package com.ssavice.network.websocket.model

import com.ssavice.model.enums.RoomType
import kotlinx.serialization.Serializable

@Serializable
sealed interface WebSocketTxEntity {
    data class SendTextChat(
        val roomId: String,
        val content: String,
        val roomType: RoomType,
    ) : WebSocketTxEntity

    data class SendRead(
        val roomId: String,
        val roomType: RoomType,
        val lastMessageId: Long,
    ) : WebSocketTxEntity

    data class NewTextDM(
        val receiverId: Long,
        val content: String,
    ) : WebSocketTxEntity

    data class SendServiceInfo(
        val roomId: String,
        val serviceId: Long,
        val roomType: RoomType,
    ) : WebSocketTxEntity

    data class NewServiceInfoDM(
        val receiverId: Long,
        val serviceId: Long,
    ) : WebSocketTxEntity
}
