package com.ssavice.network.di

import com.ssavice.network.websocket.ChatWebSocketManager
import com.ssavice.network.websocket.WebSocketMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    @Provides
    @Singleton
    fun provideChatWebSocketManager(okHttpClient: OkHttpClient): ChatWebSocketManager =
        ChatWebSocketManager
            .builder()
            .addJson(Json { ignoreUnknownKeys = true })
            .addClient(okHttpClient)
            .addMapper(
                WebSocketMapper(),
            ).build()
}
