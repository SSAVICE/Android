package com.ssavice.chat.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ssavice.chat.ChatRemoteMediator
import com.ssavice.room.ChatDatabase
import com.ssavice.room.dto.Chat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val chatDatabase: ChatDatabase
) : ChatRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getChatMessages(roomId: String): Flow<PagingData<Chat>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = ChatRemoteMediator(
                roomId = roomId,
                chatApi = chatApi,
                chatDatabase = chatDatabase
            ),
            pagingSourceFactory = {
                chatDatabase.chatDao().getChatPagingSource(roomId)
            }
        ).flow
    }

    override fun sendChat(roomId: String, message: String) {
        TODO("Not yet implemented")
    }
}
