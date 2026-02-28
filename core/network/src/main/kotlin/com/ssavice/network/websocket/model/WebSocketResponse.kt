package com.ssavice.network.websocket.model

import com.ssavice.model.enums.RoomType
import kotlinx.serialization.Serializable

enum class WebSocketMessageType(val value: String) {
    TEXT("TEXT"),
    IMAGE("IMAGE"),
    INFO("INFO"),
    READ("READ"),
    UNKNOWN("UNKNOWN");

    companion object
}

fun WebSocketMessageType.Companion.mapType(value: String): WebSocketMessageType {
    return try {
        WebSocketMessageType.valueOf(value.uppercase())
    } catch (e: IllegalArgumentException) {
        WebSocketMessageType.UNKNOWN
    }
}

@Serializable
data class WebSocketResponse(
    val messageId: Long,
    val messageType: String,
    val roomType: RoomType,
    val roomId: String,
    val receiver: Long,
    val sender: Long,
    val message: String,
    val serviceId: Long,
    val createdAt: String,
    val readMsgId: Long,
)
