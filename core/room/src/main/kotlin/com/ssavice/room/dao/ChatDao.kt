package com.ssavice.room.dao

import android.util.Log
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ssavice.room.dto.ChatEntity

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id DESC")
    fun getChatPagingSource(roomId: String): PagingSource<Int, ChatEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(chats: List<ChatEntity>)

    @Query("DELETE FROM chat WHERE roomId = :roomId")
    suspend fun clearAllByRoomId(roomId: String)

    @Query("SELECT * FROM chat WHERE id = :id")
    suspend fun getChatById(id: Int): List<ChatEntity>

    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id ASC LIMIT 1")
    suspend fun getFirstChat(roomId: String): ChatEntity?

    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id DESC LIMIT 1")
    suspend fun getLastChat(roomId: String): ChatEntity?

    @Transaction
    suspend fun insertIfContinuous(chat: ChatEntity) {
        val lastChat = getLastChat(chat.roomId)

        if (lastChat == null || chat.id == lastChat.id + 1) {
            insertAll(listOf(chat))
        } else {
            Log.d("ChatDao", "(chat ${chat.id}) 채팅이 연속적이지 않습니다. 해당 입력을 무시합니다.")
        }
    }

    @Query("DELETE FROM chat")
    suspend fun removeAll()
}
