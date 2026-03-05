package com.ssavice.model.chat

import com.ssavice.model.enums.RoomType

data class ChattingRoomInfo(
    val roomId: String,
    val name: String,
    val roomType: RoomType,
    val serviceId: Long,
    val participantIds: List<Long>,
)
