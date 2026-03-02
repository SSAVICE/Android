package com.ssavice.chat

import com.ssavice.model.DateTime

sealed interface ChatMessage {
    val messageId: Long
    val time: DateTime
    val userId: Long

    data class TextMessage(
        val you: Boolean,
        val first: Boolean,
        val text: String,
        override val userId: Long,
        override val time: DateTime,
        override val messageId: Long,
    ) : ChatMessage

    data class ServiceMessage(
        val you: Boolean,
        val serviceId: Long,
        override val userId: Long,
        override val time: DateTime,
        override val messageId: Long,
    ) : ChatMessage
}
