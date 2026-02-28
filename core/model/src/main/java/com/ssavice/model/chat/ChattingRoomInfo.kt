package com.ssavice.model.chat

import com.ssavice.model.enums.RoomType

data class ChattingRoomInfo(
    val roomId: String,
    val name: String,
    val roomType: RoomType,
    val serviceId: Long,
    val participants: List<ChattingRoomParticipant>,
)

data class ChattingRoomParticipant(
    val name: String,
    val userId: Long,
    val thumbnail: String,
)
