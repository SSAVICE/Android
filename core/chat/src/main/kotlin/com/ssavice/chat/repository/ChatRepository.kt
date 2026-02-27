package com.ssavice.chat.repository

import androidx.paging.PagingData
import com.ssavice.chat.model.network.GetRoomInfoDTO
import com.ssavice.model.chat.Chat
import com.ssavice.model.chat.ChattingRoomMetadata
import com.ssavice.room.dto.ChatEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatMessages(roomId: Long): Flow<PagingData<Chat>>

    suspend fun sendChat(roomId: Long, message: String)

    suspend fun setLastReadMessageId(roomId: Long, messageId: Int)

    fun getRoomList(): Flow<List<ChattingRoomMetadata>>

    suspend fun getRoomInfo(roomId: Long): Result<GetRoomInfoDTO>
}
