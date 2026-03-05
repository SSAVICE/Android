package com.ssavice.chat.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ssavice.chat.ChatRemoteMediator
import com.ssavice.chat.WebSocketEventHandler
import com.ssavice.chat.model.enum.ChatCursorDirection
import com.ssavice.chat.service.ChatRetrofitService
import com.ssavice.datastore.repository.ChattingMetadataRepository
import com.ssavice.model.chat.Chat
import com.ssavice.model.chat.ChattingRoomInfo
import com.ssavice.model.chat.ChattingRoomMetadata
import com.ssavice.model.chat.ChattingServiceSummary
import com.ssavice.model.chat.ChattingUserInfo
import com.ssavice.model.enums.RoomType
import com.ssavice.model.enums.getValue
import com.ssavice.network.retrofit.service.ServiceRetrofitService
import com.ssavice.network.retrofit.service.UserInfoRetrofitService
import com.ssavice.network.websocket.ChatWebSocketManager
import com.ssavice.network.websocket.model.WebSocketRxEntity
import com.ssavice.network.websocket.model.WebSocketTxEntity
import com.ssavice.room.ChatDatabase
import com.ssavice.room.dao.ChatDao
import com.ssavice.room.dao.ChatRoomDao
import com.ssavice.room.dto.ChatEntity
import com.ssavice.room.dto.ChatRoomEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.collections.filter

class ChatRepositoryImpl
    @Inject
    constructor(
        private val chatApi: ChatRetrofitService,
        private val chatDao: ChatDao,
        private val chatRoomDao: ChatRoomDao,
        private val chatDatabase: ChatDatabase,
        private val webSocketManager: ChatWebSocketManager,
        private val eventHandler: WebSocketEventHandler,
        private val userRetrofitService: UserInfoRetrofitService,
        private val serviceRetrofitSummary: ServiceRetrofitService,
        private val metadataRepository: ChattingMetadataRepository,
    ) : ChatRepository {
        init {
            eventHandler.startObserving()
        }

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
                            chatRepository = this,
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
            roomType: RoomType,
        ) {
            webSocketManager.sendMessage(
                WebSocketTxEntity.SendTextChat(
                    roomId = roomId,
                    content = message,
                    roomType = roomType,
                ),
            )
        }

        override suspend fun startChat(
            opponentId: Long,
            serviceId: Long,
            message: String,
        ) {
            webSocketManager.sendMessage(
                WebSocketTxEntity.NewServiceInfoDM(
                    serviceId = serviceId,
                    receiverId = opponentId,
                ),
            )
            delay(100)
            webSocketManager.sendMessage(
                WebSocketTxEntity.NewTextDM(
                    receiverId = opponentId,
                    content = message,
                ),
            )
        }

        override suspend fun setLastReadMessageId(
            roomId: String,
            messageId: Int,
        ) {
            chatRoomDao.updateLastReadIdIfGreater(roomId, messageId)
        }

        private fun mapLastMessageAtToMilliseconds(lastMessageAt: List<Int>): Long =
            try {
                // 리스트의 각 인덱스: 0:연, 1:월, 2:일, 3:시, 4:분, 5:초
                if (lastMessageAt.size >= 6) {
                    java.time.LocalDateTime
                        .of(
                            lastMessageAt[0], // Year
                            lastMessageAt[1], // Month
                            lastMessageAt[2], // Day
                            lastMessageAt[3], // Hour
                            lastMessageAt[4], // Minute
                            lastMessageAt[5], // Second
                        ).toInstant(java.time.ZoneOffset.UTC)
                        .toEpochMilli()
                } else {
                    // 데이터가 불완전할 경우 처리 (예: 현재 시간 반환)
                    System.currentTimeMillis()
                }
            } catch (e: Exception) {
                // 파싱 에러 시 기본값
                0L
            }

        override fun getRoomList(): Flow<List<ChattingRoomMetadata>> {
            CoroutineScope(Dispatchers.IO).launch {
                chatApi.getRoomList().onSuccess { result ->
                    result.rooms.forEach { room ->
                        chatRoomDao.upsertRoomMetadata(
                            ChatRoomEntity(
                                roomId = room.roomId,
                                lastReadMessageId = 0,
                                lastMessageId = room.lastChatId ?: 0,
                                roomName = room.name,
                                lastMessage = room.lastMessage ?: "",
                                lastMessageCreatedAt = mapLastMessageAtToMilliseconds(room.lastMessageAt),
                                roomType = RoomType.getValue(room.type),
                                serviceId = room.serviceId ?: -1,
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

        override suspend fun getRoomInfo(roomId: String): Result<ChattingRoomInfo> =
            chatApi.getRoomInfo(roomId).map {
                it.toModel()
            }

        override suspend fun getMessages(
            roomId: String,
            cursor: Long,
            size: Int,
            direction: ChatCursorDirection,
        ): Result<List<ChatEntity>> {
            val response =
                chatApi.getChatList(
                    roomId = roomId,
                    cursor = cursor,
                    size = size,
                    direction = direction.value,
                )

            return response.map { data ->
                data.messages.map {
                    ChatEntity(
                        id = it.messageId,
                        userId = it.sender,
                        roomId = it.roomId,
                        type = it.messageType,
                        content = it.message,
                        createdAt = mapLastMessageAtToMilliseconds(it.createdAt),
                    )
                }
            }
        }

        override suspend fun readyForAck(
            serviceId: Long,
            userId: Long,
        ): Flow<Result<String>> =
            webSocketManager.events
                .filterIsInstance<WebSocketRxEntity.ServiceInfoChat>()
                .filter { event ->
                    event.serviceId == serviceId && event.senderId == userId
                }.map { event ->
                    Result.success(event.roomId)
                }.take(1)
                .let { flow ->
                    flow {
                        try {
                            withTimeout(10000L) {
                                flow.collect { emit(it) }
                            }
                        } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
                            emit(Result.failure(Exception("Ack 타임아웃: 서버 응답이 없습니다.")))
                        } catch (e: Exception) {
                            emit(Result.failure(e))
                        }
                    }
                }

        override fun getUserInfoMap(): Flow<Map<Long, ChattingUserInfo>> = metadataRepository.getUserInfoFlow()

        override suspend fun getMyId(): Result<Long> = userRetrofitService.getMyUserId().map { it.id }

        override fun getChatServiceSummaryMap(): Flow<Map<Long, ChattingServiceSummary>> = metadataRepository.getServiceSummaryFlow()

        private suspend fun getIfUserInfoNeedUpdate(userId: Long): Boolean {
            val data = metadataRepository.getUserInfo(userId)
            data.onSuccess {
                return it.needRefresh()
            }
            return true
        }

        private suspend fun getUserInfoFromRemoteAndUpdate(userId: List<Long>): Result<Unit> {
            val result = userRetrofitService.getUserInfoSummary(userId)
            return result
                .map { it.toModel() }
                .onSuccess { data ->
                    metadataRepository.setUserInfos(data)
                }.map { Unit }
        }

        override suspend fun updateUserInfoIfNeed(userIds: List<Long>): Result<Unit> {
            val updateList = userIds.filter { getIfUserInfoNeedUpdate(it) }
            return getUserInfoFromRemoteAndUpdate(updateList)
        }

        override suspend fun updateServiceSummaryIfNeed(serviceId: Long): Result<Unit> {
            val result = serviceRetrofitSummary.getServiceSummary(serviceId)
            return result
                .map { it.toModel(serviceId) }
                .onSuccess { data ->
                    metadataRepository.setServiceSummary(data)
                }.map { Unit }
        }
    }
