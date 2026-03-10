package com.ssavice.room.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ssavice.room.dto.ChatRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoomMetadata(room: ChatRoomEntity): Long

    @Query("SELECT * FROM chat_rooms WHERE roomId = :roomId")
    suspend fun getRoomMetadata(roomId: String): ChatRoomEntity?

    @Query("SELECT * FROM chat_rooms")
    fun getAllRoomsFlow(): Flow<List<ChatRoomEntity>>

    @Query(
        "UPDATE chat_rooms SET lastReadMessageId = :messageId " +
            "WHERE roomId = :roomId AND lastReadMessageId < :messageId",
    )
    suspend fun updateLastReadIdIfGreater(
        roomId: String,
        messageId: Int,
    )

    @Query("DELETE FROM chat_rooms")
    suspend fun removeAll()

    @Query(
        """
        UPDATE chat_rooms 
        SET lastMessage = :lastMessage, 
            lastMessageId = :lastMessageId, 
            lastMessageCreatedAt = :lastMessageCreatedAt 
        WHERE roomId = :roomId AND lastReadMessageId <= :lastMessageId
    """,
    )
    suspend fun updateRoomLastMessage(
        roomId: String,
        lastMessage: String,
        lastMessageId: Long,
        lastMessageCreatedAt: Long,
    )

    @Transaction
    suspend fun upsertRoomMetadata(room: ChatRoomEntity) {
        Log.d(
            "ChatRoomDao",
            "upsertRoomMetadata: roomId=${room.roomId}, lastMessage=${room.lastMessage}",
        )
        val result = insertRoomMetadata(room)
        if (result == -1L) { // -1은 IGNORE되어 삽입되지 않았음을 의미
            Log.d(
                "ChatRoomDao",
                "Room duplicate. Updating Room info: roomId=${room.roomId}, lastMessage=${room.lastMessage}",
            )
            try {
                updateRoomLastMessage(
                    room.roomId,
                    room.lastMessage,
                    room.lastMessageId,
                    room.lastMessageCreatedAt,
                )
            } catch (e: Exception) {
                Log.e("ChatRoomDao", "Error updating room last message", e)
            }
        }
    }
}
