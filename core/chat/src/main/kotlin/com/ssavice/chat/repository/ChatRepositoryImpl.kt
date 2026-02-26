package com.ssavice.chat.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ssavice.chat.ChatRemoteMediator
import com.ssavice.chat.service.ChatRetrofitService
import com.ssavice.room.ChatDatabase
import com.ssavice.room.dto.ChatEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatRetrofitService,
    private val chatDatabase: ChatDatabase
) : ChatRepository {

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override fun getChatMessages(roomId: Long): Flow<PagingData<ChatEntity>> = flow {
        val lastChat = chatDatabase.chatDao().getLastChat(roomId)
        emit(lastChat?.id)
    }.flatMapLatest {
        lastMessageId ->
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = ChatRemoteMediator(
                roomId = roomId,
                chatApi = chatApi,
                chatDatabase = chatDatabase,
                initialMessageId = lastMessageId
            ),
            pagingSourceFactory = {
                chatDatabase.chatDao().getChatPagingSource(roomId)
            }
        ).flow
    }

    override fun sendChat(roomId: Long, message: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setLastReadMessageId(roomId: Long, messageId: Int) {
        chatDatabase.chatRoomDao().updateLastReadId(roomId, messageId)
    }
}
