package com.ssavice.chat.service

import com.ssavice.chat.model.network.GetChatMessageDTO
import com.ssavice.chat.model.network.GetRoomInfoDTO
import com.ssavice.chat.model.network.GetRoomListDTO
import com.ssavice.room.dto.ChatEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatRetrofitService {
    @GET("/api/chat/messages")
    suspend fun getChatList(
        @Query("cursor") cursor: Long,
        @Query("direction") direction: String,
        @Query("roomId") roomId: String,
        @Query("size") size: Int,
    ): Response<GetChatMessageDTO>

    @GET("/api/room/list")
    suspend fun getRoomList(): Response<GetRoomListDTO>

    @GET("/api/room")
    suspend fun getRoomInfo(
        @Query("room-id") roomId: String,
    ): Response<GetRoomInfoDTO>
}
