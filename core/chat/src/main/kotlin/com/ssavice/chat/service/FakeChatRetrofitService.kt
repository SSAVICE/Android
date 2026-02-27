package com.ssavice.chat.service

import android.util.Log
import androidx.compose.ui.util.fastCoerceAtLeast
import com.ssavice.chat.model.network.GetRoomInfoDTO
import com.ssavice.chat.model.network.GetRoomListDTO
import com.ssavice.chat.model.network.RoomDTO
import com.ssavice.chat.model.network.RoomParticipantDTO
import com.ssavice.room.dto.ChatEntity
import retrofit2.Response

class FakeChatRetrofitService : ChatRetrofitService {
    val lastId = 500L
    override suspend fun getChatList(
        cursor: Long,
        direction: String,
        roomId: Long,
        size: Int
    ): Response<List<ChatEntity>> {
        Log.d("ChatRetrofitService", "getChatList")
        val ids: List<Long> = when (direction) {
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
        val data = ids.map {
            ChatEntity(
                id = it.toInt(),
                userId = 1,
                roomId = roomId,
                type = "TEXT",
                content = "Hello, Chatting ($it)!",
                createdAt = System.currentTimeMillis() - 86400000L * 10 + it * 60000L
            )
        }
        return Response.success(
            data
        )
    }

    override suspend fun getRoomList(): Response<GetRoomListDTO> {
        Log.d("ChatRetrofitService", "getRoomList")
        return Response.success(
            GetRoomListDTO(
                (0..10).map { id ->
                    RoomDTO(
                        roomId = id.toLong(),
                        name = "room $id",
                        lastMessage = "last text ($id)",
                        lastChatId = lastId,
                        serviceId = 1,
                        type = "DM",
                    )
                }
            )
        )
    }

    override suspend fun getRoomInfo(roomId: Long): Response<GetRoomInfoDTO> {
        return Response.success(
            GetRoomInfoDTO(
                roomId = roomId,
                name = "room 1",
                roomType = "DM",
                serviceId = 1,
                participants = listOf(
                    RoomParticipantDTO(
                        name = "User 1",
                        userId = 1,
                        thumbnail = ""
                    ), RoomParticipantDTO(
                        name = "User 2",
                        userId = 2,
                        thumbnail = ""
                    )
                )
            )
        )
    }
}
