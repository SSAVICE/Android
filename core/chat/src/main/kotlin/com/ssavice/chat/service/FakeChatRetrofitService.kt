package com.ssavice.chat.service

import android.util.Log
import androidx.compose.ui.util.fastCoerceAtLeast
import com.ssavice.chat.model.network.GetRoomListDTO
import com.ssavice.chat.model.network.RoomDTO
import com.ssavice.room.dto.ChatEntity
import retrofit2.Response

class FakeChatRetrofitService : ChatRetrofitService {
    val lastId = 300L
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
                (lastId-size+1 .. lastId).toList()
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
                        lastChatId = 300,
                        serviceId = 1,
                        type = "DM",
                    )
                }
            )
        )
    }
}
