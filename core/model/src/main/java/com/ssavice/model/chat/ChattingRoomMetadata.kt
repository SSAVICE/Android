package com.ssavice.model.chat

import com.ssavice.model.enums.RoomType

data class ChattingRoomMetadata(
    val name: String,
    val lastUpdate: Long,
    val lastMessage: String,
    val lastMessageId: Long,
    val unreadCount: Int,
    val roomId: String,
    val roomType: RoomType
)
