package com.ssavice.chat

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ssavice.chat.model.enum.ChatCursorDirection
import com.ssavice.chat.repository.ChatRepository
import com.ssavice.chat.service.ChatRetrofitService
import com.ssavice.common.DomainFormatter
import com.ssavice.network.processResponseOnResponseData
import com.ssavice.room.ChatDatabase
import com.ssavice.room.dao.ChatDao
import com.ssavice.room.dao.ChatRoomDao
import com.ssavice.room.dto.ChatEntity

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
    private val roomId: String,
    private val initialMessageId: Long?,
    private val chatRepository: ChatRepository,
    private val chatDao: ChatDao,
    private val chatDatabase: ChatDatabase,
) : RemoteMediator<Int, ChatEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatEntity>,
    ): MediatorResult {
        return try {
            // 1. 페이징 지점 파악 (현재 어느 위치까지 로드했는지)
            Log.d("ChatRemoteMediator", "Getting data from backend, loadType: ${loadType.name}, size: ${state.config.pageSize}")
            val lastId: Long
            val cursorDirection: ChatCursorDirection
            when (loadType) {
                LoadType.PREPEND -> {
                    val firstItem =
                        state.firstItemOrNull() ?: return MediatorResult.Success(
                            endOfPaginationReached = false,
                        )
                    cursorDirection = ChatCursorDirection.AFTER
                    lastId = firstItem.id
                }

                LoadType.APPEND -> {
                    val lastItem =
                        state.lastItemOrNull() ?: return MediatorResult.Success(
                            endOfPaginationReached = false,
                        )
                    cursorDirection = ChatCursorDirection.BEFORE
                    lastId = lastItem.id
                }

                else -> {
                    lastId = initialMessageId ?: 1
                    cursorDirection = if (initialMessageId != null) ChatCursorDirection.AFTER else ChatCursorDirection.LATEST
                }
            }
            Log.d("ChatRemoteMediator", "cursor: $lastId, direction: ${cursorDirection.value}, size: ${state.config.pageSize}")

            // 2. 네트워크 호출
            val response =
                chatRepository.getMessages(
                    roomId = roomId,
                    cursor = lastId,
                    size = state.config.pageSize,
                    direction = cursorDirection,
                )

            response.fold(
                onSuccess = { data ->
                    chatDatabase.withTransaction {
                        chatDao.insertAll(data)
                    }
                    Log.d("ChatRemoteMediator", "data insertion Success. count: ${data.size}")

                    MediatorResult.Success(endOfPaginationReached = data.isEmpty() && loadType != LoadType.REFRESH)
                },
                onFailure = {
                    Log.d("ChatRemoteMediator", "data insertion Failed")
                    MediatorResult.Error(it)
                },
            )
        } catch (e: Exception) {
            Log.d("ChatRemoteMediator", "failed", e)
            MediatorResult.Error(e)
        }
    }
}
