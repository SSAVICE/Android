package com.ssavice.chat.model.network

import kotlinx.serialization.Serializable

@Serializable
data class GetChatMessageDTO(
    val messages: List<MessageDTO>,
)

@Serializable
data class MessageDTO(
    val createdAt: List<Int>,
    val message: String,
    val messageId: Long,
    val messageType: String,
    val roomId: String,
    val roomType: String,
    val serviceId: Long,
    val sender: Long,
)
