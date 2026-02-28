package com.ssavice.chat.repository

import androidx.paging.PagingData
import com.ssavice.chat.model.network.GetRoomInfoDTO
import com.ssavice.model.chat.Chat
import com.ssavice.model.chat.ChattingRoomInfo
import com.ssavice.model.chat.ChattingRoomMetadata
import com.ssavice.model.enums.RoomType
import com.ssavice.room.dto.ChatEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessages(roomId: String): Flow<PagingData<Chat>>

    suspend fun sendChat(
        roomId: String,
        message: String,
        roomType: RoomType
    )

    suspend fun setLastReadMessageId(
        roomId: String,
        messageId: Int,
    )

    fun getRoomList(): Flow<List<ChattingRoomMetadata>>

    suspend fun getRoomInfo(roomId: String): Result<ChattingRoomInfo>
}
