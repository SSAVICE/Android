package com.ssavice.room.dto

import androidx.room.Entity

@Entity(tableName = "chat_remote_keys", primaryKeys = ["messageId", "roomId"])
data class ChatRemoteKeys(
    val messageId: Int,
    val roomId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
