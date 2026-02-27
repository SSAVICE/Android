package com.ssavice.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssavice.model.chat.ChattingRoomMetadata

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

    fun toModel(): ChattingRoomMetadata = ChattingRoomMetadata(
        name = roomName,
        lastUpdate = lastMessageCreatedAt,
        lastMessage = lastMessage,
        lastMessageId = lastMessageId.toLong(),
        unreadCount = unreadCount,
        roomId = roomId
    )

}
