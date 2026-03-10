package com.ssavice.chat.service

import android.util.Log
import androidx.compose.ui.util.fastCoerceAtLeast
import com.ssavice.chat.model.network.GetChatMessageDTO
import com.ssavice.chat.model.network.GetRoomInfoDTO
import com.ssavice.chat.model.network.GetRoomListDTO
import com.ssavice.chat.model.network.MessageDTO
import com.ssavice.chat.model.network.RoomDTO

class FakeChatRetrofitService : ChatRetrofitService {
    val lastId = 500L

    override suspend fun getChatList(
        cursor: Long,
        direction: String,
        roomId: String,
        size: Int,
    ): Result<GetChatMessageDTO> {
        Log.d("ChatRetrofitService", "getChatList")
        val ids: List<Long> =
            when (direction) {
                "BEFORE" -> {
                    ((cursor - size).fastCoerceAtLeast(0) until cursor).toList()
                }

                "AFTER" -> {
                    (cursor + 1..(cursor + size).coerceAtMost(lastId)).toList()
                }

                else -> {
                    (lastId - size + 1..lastId).toList()
                }
            }
        val data =
            GetChatMessageDTO(
                ids.map {
                    MessageDTO(
                        messageId = it,
                        sender = 1,
                        roomId = roomId,
                        messageType = "TEXT",
                        message = "Hello, Chatting ($it)!",
                        createdAt = listOf(),
                        roomType = "DM",
                        serviceId = 1,
                    )
                },
            )
        return Result.success(
            data,
        )
    }

    override suspend fun getRoomList(): Result<GetRoomListDTO> {
        Log.d("ChatRetrofitService", "getRoomList")
        return Result.success(
            GetRoomListDTO(
                (0..10).map { id ->
                    RoomDTO(
                        roomId = "room $id",
                        name = "room $id",
                        lastMessage = "last text ($id)",
                        lastChatId = lastId,
                        serviceId = 1,
                        type = "DM",
                        lastMessageAt = listOf(2026, 3, 2, 6, 40, 15),
                        unReadMsgCnt = 0,
                        memberCnt = 2,
                    )
                },
            ),
        )
    }

    override suspend fun getRoomInfo(roomId: String): Result<GetRoomInfoDTO> =
        Result.success(
            GetRoomInfoDTO(
                roomId = roomId,
                name = "room 1",
                roomType = "DM",
                serviceId = 1,
                members = mapOf(),
            ),
        )
}
