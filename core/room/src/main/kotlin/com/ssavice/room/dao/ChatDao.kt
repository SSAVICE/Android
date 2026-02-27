package com.ssavice.room.dao

import android.util.Log
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ssavice.room.dto.ChatEntity

@Dao
interface ChatDao {
    // 1. 특정 채팅방의 메시지를 최신순으로 가져오기 (Paging3용)
    // RemoteMediator가 데이터를 로컬에 채워넣으면 이 Query가 자동으로 업데이트를 감지합니다.
    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id DESC")
    fun getChatPagingSource(roomId: Long): PagingSource<Int, ChatEntity>

    // 2. 네트워크에서 가져온 데이터를 로컬 DB에 삽입 (RemoteMediator에서 사용)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(chats: List<ChatEntity>)

    // 3. 특정 채팅방의 데이터만 초기화 (새로고침 시 필요할 수 있음)
    @Query("DELETE FROM chat WHERE roomId = :roomId")
    suspend fun clearAllByRoomId(roomId: Long)

    @Query("SELECT * FROM chat WHERE id = :id")
    suspend fun getChatById(id: Int): List<ChatEntity>

    // 4. (선택사항) 가장 마지막 메시지 조회 (RemoteMediator에서 페이징 지점 찾기용)
    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id ASC LIMIT 1")
    suspend fun getFirstChat(roomId: Long): ChatEntity?

    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id DESC LIMIT 1")
    suspend fun getLastChat(roomId: Long): ChatEntity?

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
