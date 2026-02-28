package com.ssavice.model.chat

data class ChattingRoomMetadata(
    val name: String,
    val lastUpdate: Long,
    val lastMessage: String,
    val lastMessageId: Long,
    val unreadCount: Int,
    val roomId: String,
)
