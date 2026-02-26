package com.ssavice.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["room_id", "id"])
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userId: Int,
    val roomId: Int,
    val type: String,
    val content: String
)
