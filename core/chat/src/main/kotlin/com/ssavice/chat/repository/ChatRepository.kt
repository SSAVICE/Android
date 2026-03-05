package com.ssavice.chat.repository

import androidx.paging.PagingData
import com.ssavice.chat.model.enum.ChatCursorDirection
import com.ssavice.chat.model.network.GetRoomInfoDTO
import com.ssavice.model.chat.Chat
import com.ssavice.model.chat.ChattingRoomInfo
import com.ssavice.model.chat.ChattingRoomMetadata
import com.ssavice.model.chat.ChattingServiceSummary
import com.ssavice.model.chat.ChattingUserInfo
import com.ssavice.model.enums.RoomType
import com.ssavice.room.dto.ChatEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessages(roomId: String): Flow<PagingData<Chat>>

    suspend fun sendChat(
        roomId: String,
        message: String,
        roomType: RoomType,
    )

    suspend fun startChat(
        opponentId: Long,
        serviceId: Long,
        message: String,
    )

    suspend fun setLastReadMessageId(
        roomId: String,
        messageId: Int,
    )

    fun getRoomList(): Flow<List<ChattingRoomMetadata>>

    suspend fun getRoomInfo(roomId: String): Result<ChattingRoomInfo>

    suspend fun getMessages(
        roomId: String,
        cursor: Long,
        size: Int,
        direction: ChatCursorDirection,
    ): Result<List<ChatEntity>>

    suspend fun readyForAck(
        serviceId: Long,
        userId: Long,
    ): Flow<Result<String>>

    fun getUserInfoMap(): Flow<Map<Long, ChattingUserInfo>>

    suspend fun getMyId(): Result<Long>

    fun getChatServiceSummaryMap(): Flow<Map<Long, ChattingServiceSummary>>

    suspend fun updateUserInfoIfNeed(
        userIds: List<Long>
    ): Result<Unit>

    suspend fun updateServiceSummaryIfNeed(
        serviceId: Long
    ): Result<Unit>
}
