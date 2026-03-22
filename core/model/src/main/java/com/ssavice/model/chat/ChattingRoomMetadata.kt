package com.ssavice.model.chat

import com.ssavice.model.enums.RoomType

data class ChattingRoomMetadata(
    val name: String,
    val lastUpdate: Long,
    val lastMessage: String,
    val lastMessageId: Long,
    val unreadCount: Int,
    val roomId: String,
    val roomType: RoomType,
    val thumbnailId: Long,
    val thumbnailUrl: String = "",
    val thumbnailUpdatedAt: Long = 0L,
) {
    fun needUpdate(): Boolean = thumbnailUpdatedAt < System.currentTimeMillis() - THUMBNAIL_UPDATE_RATE
}

const val THUMBNAIL_UPDATE_RATE = 3600000L
