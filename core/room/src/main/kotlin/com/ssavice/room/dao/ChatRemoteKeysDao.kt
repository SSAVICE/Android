package com.ssavice.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssavice.room.dto.ChatRemoteKeys

@Dao
interface ChatRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ChatRemoteKeys>)

    @Query("SELECT * FROM chat_remote_keys WHERE messageId = :messageId AND roomId = :roomId")
    suspend fun remoteKeysId(messageId: Int, roomId: String): ChatRemoteKeys?

    @Query("DELETE FROM chat_remote_keys WHERE roomId = :roomId")
    suspend fun clearRemoteKeys(roomId: String)

    @Query("DELETE FROM chat_remote_keys")
    suspend fun removeAll()
}
