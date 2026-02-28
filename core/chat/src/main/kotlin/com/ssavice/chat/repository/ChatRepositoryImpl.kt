package com.ssavice.chat.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ssavice.chat.ChatRemoteMediator
import com.ssavice.chat.model.network.GetRoomInfoDTO
import com.ssavice.chat.service.ChatRetrofitService
import com.ssavice.model.chat.Chat
import com.ssavice.model.chat.ChattingRoomMetadata
import com.ssavice.network.processResponseOnResponseData
import com.ssavice.room.ChatDatabase
import com.ssavice.room.dao.ChatDao
import com.ssavice.room.dao.ChatRoomDao
import com.ssavice.room.dto.ChatEntity
import com.ssavice.room.dto.ChatRoomEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatRepositoryImpl
    @Inject
    constructor(
        private val chatApi: ChatRetrofitService,
        private val chatDao: ChatDao,
        private val chatRoomDao: ChatRoomDao,
        private val chatDatabase: ChatDatabase,
    ) : ChatRepository {
        @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
        override fun getChatMessages(roomId: String): Flow<PagingData<Chat>> =
            flow {
                val lastChat = chatDao.getLastChat(roomId)
                val lastReadChat = chatRoomDao.getRoomMetadata(roomId)?.lastReadMessageId
                emit(lastChat?.id?.coerceAtLeast(lastReadChat ?: 0))
            }.flatMapLatest { lastMessageId ->
                Pager(
                    config =
                        PagingConfig(
                            pageSize = 20,
                            initialLoadSize = 40,
                            enablePlaceholders = true,
                            prefetchDistance = 5,
                        ),
                    remoteMediator =
                        ChatRemoteMediator(
                            roomId = roomId,
                            chatApi = chatApi,
                            chatDao = chatDao,
                            initialMessageId = lastMessageId,
                            chatDatabase = chatDatabase,
                        ),
                    pagingSourceFactory = {
                        chatDao.getChatPagingSource(roomId)
                    },
                ).flow.map { pagingData ->
                    pagingData.map { entity ->
                        entity.toModel()
                    }
                }
            }

        override suspend fun sendChat(
            roomId: String,
            message: String,
        ) {
            val lastId = chatDao.getLastChat(roomId)?.id ?: -1
            Log.d("ChatRepositoryImpl", "lastId: $lastId")
            chatDao.insertAll(
                listOf(
                    ChatEntity(
                        id = lastId + 1,
                        userId = 1,
                        roomId = roomId,
                        type = "TEXT",
                        content = message,
                        createdAt = System.currentTimeMillis(),
                    ),
                ),
            )
        }

        override suspend fun setLastReadMessageId(
            roomId: String,
            messageId: Int,
        ) {
            chatRoomDao.updateLastReadId(roomId, messageId)
        }

        override fun getRoomList(): Flow<List<ChattingRoomMetadata>> {
            CoroutineScope(Dispatchers.IO).launch {
                processResponseOnResponseData(chatApi.getRoomList()).onSuccess { result ->
                    result.rooms.forEach { room ->
                        chatRoomDao.upsertRoomMetadata(
                            ChatRoomEntity(
                                roomId = room.roomId,
                                lastReadMessageId = 0,
                                lastMessageId = room.lastChatId?.toInt() ?: 0,
                                roomName = room.name,
                                lastMessage = room.lastMessage ?: "",
                                lastMessageCreatedAt = room.lastChatId ?: 0,
                            ),
                        )
                    }
                }
            }
            return chatRoomDao.getAllRoomsFlow().map {
                it.map { entity ->
                    entity.toModel()
                }
            }
        }

        override suspend fun getRoomInfo(roomId: String): Result<GetRoomInfoDTO> = processResponseOnResponseData(chatApi.getRoomInfo(roomId))
    }
