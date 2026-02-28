package com.ssavice.model.chat

data class Chat(
    val messageId: Long,
    val senderId: Long,
    val roomId: String,
    val type: String,
    val content: String,
    val createdAt: Long,
)
