package com.ssavice.chat_list

import com.ssavice.model.DateTime

data class ChattingRoomItem(
    val name: String,
    val serviceId: Long?,
    val serviceName: String,
    val lastUpdate: DateTime,
    val lastUpdateString: String,
    val unreadCount: Int,
    val roomId: String,
    val lastMessage: String? = null,
    val thumbnail: String
)
