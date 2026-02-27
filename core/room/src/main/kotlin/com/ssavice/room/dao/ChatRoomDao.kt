package com.ssavice.room.dao

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
    suspend fun getRoomMetadata(roomId: Long): ChatRoomEntity?

    @Query("SELECT * FROM chat_rooms")
    fun getAllRoomsFlow(): Flow<List<ChatRoomEntity>>

    // 내가 메시지를 읽었을 때 업데이트
    @Query("UPDATE chat_rooms SET lastReadMessageId = :messageId WHERE roomId = :roomId")
    suspend fun updateLastReadId(
        roomId: Long,
        messageId: Int,
    )

    // 서버에서 새 메시지가 왔을 때 업데이트
    @Query(
        "UPDATE chat_rooms SET lastMessageId = :messageId, lastMessage = :lastMessage, lastMessageCreatedAt = :lastMessageCreatedAt WHERE roomId = :roomId",
    )
    suspend fun updateServerLastId(
        roomId: Long,
        messageId: Int,
        lastMessage: String,
        lastMessageCreatedAt: Long,
    )

    @Query(
        "UPDATE chat_rooms SET lastReadMessageId = :messageId " +
            "WHERE roomId = :roomId AND lastReadMessageId < :messageId",
    )
    suspend fun updateLastReadIdIfGreater(
        roomId: Long,
        messageId: Int,
    )

    @Query("DELETE FROM chat_rooms")
    suspend fun removeAll()

    // 특정 필드만 업데이트 (중복 시 사용)
    @Query(
        """
        UPDATE chat_rooms 
        SET lastMessage = :lastMessage, 
            lastMessageId = :lastMessageId, 
            lastMessageCreatedAt = :lastMessageCreatedAt 
        WHERE roomId = :roomId
    """,
    )
    suspend fun updateRoomLastMessage(
        roomId: Long,
        lastMessage: String,
        lastMessageId: Int,
        lastMessageCreatedAt: Long,
    )

    // Upsert 로직 (위의 두 기능을 합친 편리한 함수)
    @Transaction
    suspend fun upsertRoomMetadata(room: ChatRoomEntity) {
        val result = insertRoomMetadata(room)
        if (result == -1L) { // -1은 IGNORE되어 삽입되지 않았음을 의미
            updateRoomLastMessage(
                room.roomId,
                room.lastMessage,
                room.lastMessageId,
                room.lastMessageCreatedAt,
            )
        }
    }
}
