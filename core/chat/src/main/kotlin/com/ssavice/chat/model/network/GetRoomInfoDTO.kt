package com.ssavice.chat.model.network

import com.ssavice.model.chat.ChattingRoomInfo
import com.ssavice.model.chat.ChattingRoomParticipant
import com.ssavice.model.enums.RoomType
import com.ssavice.model.enums.getValue
import kotlinx.serialization.Serializable

@Serializable
data class GetRoomInfoDTO(
    val roomId: String,
    val name: String,
    val roomType: String,
    val serviceId: Long?,
    val participants: List<RoomParticipantDTO>,
) {
    fun toModel(): ChattingRoomInfo =
        ChattingRoomInfo(
            roomId = roomId,
            name = name,
            roomType = RoomType.getValue(roomType),
            serviceId = serviceId ?: 0,
            participants = participants.map { it.toModel() }
        )
}

@Serializable
data class RoomParticipantDTO(
    val name: String,
    val userId: Long,
    val thumbnail: String?,
){
    fun toModel(): ChattingRoomParticipant =
        ChattingRoomParticipant(
            name = name,
            userId = userId,
            thumbnail = thumbnail?:""
        )
}
