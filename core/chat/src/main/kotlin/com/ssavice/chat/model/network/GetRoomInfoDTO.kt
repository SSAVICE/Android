package com.ssavice.chat.model.network

import com.ssavice.model.chat.ChattingRoomInfo
import com.ssavice.model.chat.ChattingRoomParticipant
import com.ssavice.model.enums.RoomType
import com.ssavice.model.enums.getValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRoomInfoDTO(
    val roomId: String,
    @SerialName("roomName")
    val name: String,
    val roomType: String,
    val serviceId: Long?,
    val members: Map<String, Long>,
) {
    fun toModel(): ChattingRoomInfo =
        ChattingRoomInfo(
            roomId = roomId,
            name = name,
            roomType = RoomType.getValue(roomType),
            serviceId = serviceId ?: 0,
        )
}

@Serializable
data class RoomParticipantDTO(
    val name: String,
    val userId: Long,
    val thumbnail: String?,
) {
    fun toModel(): ChattingRoomParticipant =
        ChattingRoomParticipant(
            name = name,
            userId = userId,
            thumbnail = thumbnail ?: "",
        )
}
