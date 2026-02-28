package com.ssavice.network.websocket.model

import kotlinx.serialization.Serializable

enum class WebSocketMessageType(val value: String) {
    TEXT("TEXT"), IMAGE("IMAGE"), INFO("INFO"), READ("READ"), UNKNOWN("UNKNOWN")
}

enum class RoomType(val value: String) {
    DM("DM"), GROUP("GROUP")
}

fun WebSocketMessageType.mapType(value: String): WebSocketMessageType {
    return try {
        WebSocketMessageType.valueOf(value.uppercase())
    } catch (e: IllegalArgumentException) {
        WebSocketMessageType.UNKNOWN
    }
}

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
