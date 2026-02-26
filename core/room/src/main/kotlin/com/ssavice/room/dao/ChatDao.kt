package com.ssavice.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssavice.room.dto.Chat

@Dao
interface ChatDao {

    // 1. 특정 채팅방의 메시지를 최신순으로 가져오기 (Paging3용)
    // RemoteMediator가 데이터를 로컬에 채워넣으면 이 Query가 자동으로 업데이트를 감지합니다.
    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id DESC")
    fun getChatPagingSource(roomId: String): PagingSource<Int, Chat>

    // 2. 네트워크에서 가져온 데이터를 로컬 DB에 삽입 (RemoteMediator에서 사용)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<Chat>)

    // 3. 특정 채팅방의 데이터만 초기화 (새로고침 시 필요할 수 있음)
    @Query("DELETE FROM chat WHERE roomId = :roomId")
    suspend fun clearAllByRoomId(roomId: String)

    @Query("SELECT * FROM chat WHERE id = :id")
    suspend fun getChatById(id: Int): List<Chat>

    // 4. (선택사항) 가장 마지막 메시지 조회 (RemoteMediator에서 페이징 지점 찾기용)
    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id ASC LIMIT 1")
    suspend fun getFirstChat(roomId: String): Chat?

    @Query("SELECT * FROM chat WHERE roomId = :roomId ORDER BY id DESC LIMIT 1")
    suspend fun getLastChat(roomId: String): Chat?
}
