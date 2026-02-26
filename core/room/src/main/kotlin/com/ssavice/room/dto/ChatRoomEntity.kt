package com.ssavice.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_rooms")
data class ChatRoomEntity(
    @PrimaryKey val roomId: Long,
    val lastReadMessageId: Int,
    val lastMessageId: Int,
    val roomName: String,
    val lastMessage: String,
    val lastMessageCreatedAt: Long
) {
    val unreadCount: Int
        get() = (lastMessageId - lastReadMessageId).coerceAtLeast(0)
}
