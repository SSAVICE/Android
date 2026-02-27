package com.ssavice.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ssavice.model.chat.Chat

@Entity(tableName = "chat", primaryKeys = ["roomId", "id"])
data class ChatEntity(
    val id: Int,
    val userId: Long,
    val roomId: Long,
    val type: String,
    val content: String,
    val createdAt: Long,
) {
    fun toModel(): Chat =
        Chat(
            messageId = id.toLong(),
            senderId = userId,
            roomId = roomId,
            type = type,
            content = content,
            createdAt = createdAt,
        )
}
