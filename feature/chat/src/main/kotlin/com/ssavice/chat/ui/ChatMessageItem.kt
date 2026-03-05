package com.ssavice.chat.ui

import androidx.compose.runtime.Composable
import coil.request.ImageRequest
import com.ssavice.designsystem.component.ChatBubble
import com.ssavice.designsystem.component.ChatBubbleDirection
import com.ssavice.designsystem.component.ChatBubbleType
import com.ssavice.model.DateTime

@Composable
fun ChatMessageItem(
    text: String,
    time: DateTime,
    simple: Boolean,
    userName: String,
    profile: String,
    end: Boolean,
    isYou: Boolean,
    imageRequestBuilder: ImageRequest.Builder,
) {
    ChatBubble(
        message = text,
        userName = userName,
        profileUrl = profile,
        timestamp = time.timeToSimpleString(),
        type = if (simple) ChatBubbleType.SIMPLE else ChatBubbleType.ALL,
        direction = if (!isYou) ChatBubbleDirection.RECEIVED else ChatBubbleDirection.SENT,
        isEnd = end,
        imageRequest = imageRequestBuilder,
    )
}
