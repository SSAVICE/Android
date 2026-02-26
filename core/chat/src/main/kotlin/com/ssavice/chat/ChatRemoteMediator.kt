package com.ssavice.chat

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ssavice.chat.model.enum.ChatCursorDirection
import com.ssavice.chat.service.ChatRetrofitService
import com.ssavice.network.processResponseOnResponseData
import com.ssavice.room.ChatDatabase
import com.ssavice.room.dto.ChatEntity

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
    private val roomId: Long,
    private val initialMessageId: Int?,
    private val chatApi: ChatRetrofitService, // Retrofit 서비스
    private val chatDatabase: ChatDatabase
) : RemoteMediator<Int, ChatEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatEntity>
    ): MediatorResult {
        return try {
            // 1. 페이징 지점 파악 (현재 어느 위치까지 로드했는지)
            val lastId: Int
            val cursorDirection: ChatCursorDirection
            when (loadType) {
                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    ) // 위쪽(과거)은 나중에 구현
                    cursorDirection = ChatCursorDirection.BEFORE
                    lastId = firstItem.id
                }

                LoadType.APPEND -> { // 아래쪽(더 오래된 데이터)으로 스크롤 시
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    cursorDirection = ChatCursorDirection.AFTER
                    lastId = lastItem.id // 마지막 채팅 ID를 기준으로 다음 페이지 요청
                }

                else -> {
                    lastId = initialMessageId ?: 0
                    cursorDirection = if (initialMessageId != null) ChatCursorDirection.AFTER else ChatCursorDirection.LATEST
                }
            }

            // 2. 네트워크 호출
            val response = processResponseOnResponseData(
                chatApi.getChatList(
                    roomId = roomId,
                    cursor = lastId.toLong(),
                    size = state.config.pageSize,
                    direction = cursorDirection.value
                )
            )

            response.fold(
                onSuccess = { data ->
                    // 3. DB 작업 (트랜잭션)
                    chatDatabase.withTransaction {
                        chatDatabase.chatDao().insertAll(data)
                    }
                    MediatorResult.Success(endOfPaginationReached = data.isEmpty())
                },
                onFailure = {
                    MediatorResult.Error(it)
                }
            )


        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
