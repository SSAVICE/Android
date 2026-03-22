package com.ssavice.chat.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRoomListDTO(
    val rooms: List<RoomDTO>,
)

@Serializable
data class RoomDTO(
    val roomId: String,
    @SerialName("roomName")
    val name: String,
    @SerialName("roomType")
    val type: String,
    val serviceId: Long?,
    @SerialName("lastMsgId")
    val lastChatId: Long?,
    @SerialName("lastMsg")
    val lastMessage: String?,
    @SerialName("lastMsgAt")
    val lastMessageAt: List<Int>?,
    val unReadMsgCnt: Int,
    val memberCnt: Int,
    val thumbnailId: Long,
)
