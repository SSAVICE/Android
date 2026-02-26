package com.ssavice.chat.service

import com.ssavice.chat.model.network.GetRoomListDTO
import com.ssavice.room.dto.Chat
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatRetrofitService {
    @GET("/chat")
    suspend fun getChatList(
        @Query("cursor") cursor: Long,
        @Query("direction") direction: String,
        @Query("roomId") roomId: Long,
        @Query("size") size: Int
        ): Response<List<Chat>>

    @GET("/chat/room")
    suspend fun getRoomList(): Response<GetRoomListDTO>
}
