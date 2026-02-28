package com.ssavice.chat.model.network

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomListDTO(
    val rooms: List<RoomDTO>,
)

@Serializable
data class RoomDTO(
    val roomId: String,
    val name: String,
    val serviceId: Long,
    val type: String,
    val lastChatId: Long?,
    val lastMessage: String?,
)
