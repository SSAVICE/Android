package com.ssavice.user_chatting

import com.ssavice.model.DateTime

data class ChattingRoomItem(
    val name: String,
    val serviceId: Long?,
    val serviceName: String,
    val lastUpdate: DateTime,
    val unreadCount: Int,
    val roomId: String,
    val lastMessage: String? = null,
)
