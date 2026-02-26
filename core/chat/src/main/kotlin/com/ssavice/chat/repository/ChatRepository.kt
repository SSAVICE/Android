package com.ssavice.chat.repository

import androidx.paging.PagingData
import com.ssavice.room.dto.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessages(roomId: String): Flow<PagingData<Chat>>

    fun sendChat(roomId: String, message: String)
}
