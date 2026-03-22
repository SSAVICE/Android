package com.ssavice.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ssavice.room.dao.ChatDao
import com.ssavice.room.dao.ChatRoomDao
import com.ssavice.room.dto.ChatEntity
import com.ssavice.room.dto.ChatRoomEntity

@Database(
    entities = [ChatEntity::class, ChatRoomEntity::class],
    version = 4,
    exportSchema = false,
)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    abstract fun chatRoomDao(): ChatRoomDao
}
