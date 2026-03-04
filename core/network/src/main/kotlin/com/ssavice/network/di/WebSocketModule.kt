package com.ssavice.network.di

import com.ssavice.network.NetworkEventManager
import com.ssavice.network.websocket.ChatWebSocketManager
import com.ssavice.network.websocket.WebSocketMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {
    @Provides
    @Singleton
    fun provideChatWebSocketManager(
        builder: OkHttpClient.Builder,
        networkEventManager: NetworkEventManager,
    ): ChatWebSocketManager =
        ChatWebSocketManager
            .builder()
            .addJson(Json { ignoreUnknownKeys = true })
            .addClient(
                builder
                    .pingInterval(30, TimeUnit.SECONDS)
                    .build(),
            ).addMapper(
                WebSocketMapper(),
            ).addNetworkEventManager(networkEventManager)
            .build()
}
