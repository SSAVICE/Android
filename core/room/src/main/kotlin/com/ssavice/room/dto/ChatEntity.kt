package com.ssavice.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat", primaryKeys = ["room_id", "id"])
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: Long,
    val roomId: Long,
    val type: String,
    val content: String,
    val createdAt: Long
)
