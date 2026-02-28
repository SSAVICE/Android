package com.ssavice.chat

import com.ssavice.network.websocket.ChatWebSocketManager
import com.ssavice.network.websocket.model.WebSocketRxEntity
import com.ssavice.room.dao.ChatDao
import com.ssavice.room.dao.ChatRoomDao
import com.ssavice.room.dto.ChatEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketEventHandler @Inject constructor(
    private val webSocketManager: ChatWebSocketManager,
    private val chatDao: ChatDao, private val chatRoomDao: ChatRoomDao,
) {
    fun startObserving() {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            webSocketManager.events.collect { event ->

            }
        }
    }

    private suspend fun handleEvent(event: WebSocketRxEntity) {
        when(event) {
            is WebSocketRxEntity.SendChat -> {

            }
        }
    }

    private suspend fun getMessage(event: WebSocketRxEntity.SendChat) {
        chatDao.insertIfContinuous(
            ChatEntity(
                id = event.messageId.toInt(),
                userId = event.senderId,
                roomId = event.roomId,
                content = event.content,
                createdAt = event.createdAt,
                type = event.type
            )
        )

        chatRoomDao.updateRoomLastMessage(
            roomId = event.roomId,
            lastMessage = event.content,
            lastMessageAt = event.createdAt,
            lastMessageType = event.type
        )
    }
}
