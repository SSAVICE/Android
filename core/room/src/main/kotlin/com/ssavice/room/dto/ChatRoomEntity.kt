package com.ssavice.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssavice.model.chat.ChattingRoomMetadata
import com.ssavice.model.enums.RoomType
import com.ssavice.model.enums.getValue

@Entity(tableName = "chat_rooms")
data class ChatRoomEntity(
    @PrimaryKey val roomId: String,
    val lastReadMessageId: Long,
    val lastMessageId: Long,
    val roomName: String,
    val lastMessage: String,
    val lastMessageCreatedAt: Long,
    val roomType: RoomType,
    val serviceId: Long,
) {
    val unreadCount: Long
        get() = (lastMessageId - lastReadMessageId).coerceAtLeast(0L)

    fun toModel(): ChattingRoomMetadata =
        ChattingRoomMetadata(
            name = roomName,
            lastUpdate = lastMessageCreatedAt,
            lastMessage = lastMessage,
            lastMessageId = lastMessageId,
            unreadCount = unreadCount.toInt(),
            roomId = roomId,
            roomType = roomType,
        )
}
