package com.ssavice.chat.model.network

import kotlinx.serialization.Serializable

@Serializable
data class GroupChatDTO(
    val messageId: String,
    val messageType: String,
    val roomType: String,
    val roomId: String,
    val sender: String,
    val message: String,
    val createdAt: String,
)

@Serializable
data class GetGroupChatDTO(
    val messageId: String,
    val messageType: String,
    val roomType: String,
)
