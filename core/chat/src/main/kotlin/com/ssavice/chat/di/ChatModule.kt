package com.ssavice.chat.di

import com.ssavice.chat.repository.ChatRepository
import com.ssavice.chat.repository.ChatRepositoryImpl
import com.ssavice.chat.service.ChatRetrofitService
import com.ssavice.network.di.RetrofitModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {
    @Binds
    @Singleton
    abstract fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

}

@Module
@InstallIn(SingletonComponent::class)
object ChatRetrofitModule {
    @Provides
    @Singleton
    fun provideChatRetrofitService(
        @RetrofitModule.ChatRetrofit chatRetrofit: Retrofit,
    ): ChatRetrofitService =
        chatRetrofit
            .create(ChatRetrofitService::class.java)
}
