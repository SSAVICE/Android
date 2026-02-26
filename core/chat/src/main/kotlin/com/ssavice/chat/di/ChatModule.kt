package com.ssavice.chat.di

import com.ssavice.chat.repository.ChatRepository
import com.ssavice.chat.repository.ChatRepositoryImpl
import com.ssavice.chat.service.ChatRetrofitService
import com.ssavice.chat.service.FakeChatRetrofitService
import com.ssavice.room.ChatDatabase
import com.ssavice.room.dao.ChatDao
import com.ssavice.room.dao.ChatRoomDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {
    @Binds
    @Singleton
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository


    companion object {
        @Provides
        @Singleton
        fun provideChatRetrofitService(): ChatRetrofitService {
            return FakeChatRetrofitService()
        }

        @Provides
        @Singleton
        fun provideChatDao(database: ChatDatabase): ChatDao {
            return database.chatDao()
        }

        @Provides
        @Singleton
        fun provideChatRoomDao(database: ChatDatabase): ChatRoomDao {
            return database.chatRoomDao()
        }
    }
}
