package com.ssavice.room.di

import android.content.Context
import androidx.room.Room
import com.ssavice.room.ChatDatabase
import com.ssavice.room.dao.ChatDao
import com.ssavice.room.dao.ChatRemoteKeysDao
import com.ssavice.room.dao.ChatRoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "chat_database"

    @Provides
    @Singleton
    fun provideChatDatabase(
        @ApplicationContext context: Context
    ): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideChatDao(database: ChatDatabase): ChatDao {
        return database.chatDao()
    }

    @Provides
    @Singleton
    fun provideChatRemoteKeysDao(database: ChatDatabase): ChatRemoteKeysDao {
        return database.remoteKeysDao()
    }

    @Provides
    @Singleton
    fun provideChatRoomDao(database: ChatDatabase): ChatRoomDao {
        return database.chatRoomDao()
    }
}
