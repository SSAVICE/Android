package com.ssavice.chat.model.network

import com.ssavice.chat.model.enum.RoomType
import kotlinx.serialization.Serializable

@Serializable
data class GetRoomListDTO(
    val rooms: List<RoomDTO>
)

@Serializable
data class RoomDTO(
    val name: String,
    val serviceId: Long,
    val type: RoomType,
    val lastChatId: Long?,
    val lastMessage: String?,
)
