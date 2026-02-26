package com.ssavice.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ssavice.room.dao.ChatDao
import com.ssavice.room.dao.ChatRemoteKeysDao
import com.ssavice.room.dto.Chat
import com.ssavice.room.dto.ChatRemoteKeys

@Database(
    entities = [Chat::class, ChatRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun remoteKeysDao(): ChatRemoteKeysDao
}
