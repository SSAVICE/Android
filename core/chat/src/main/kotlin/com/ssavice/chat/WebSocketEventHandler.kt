package com.ssavice.chat

import android.util.Log
import com.ssavice.model.enums.ChatType
import com.ssavice.network.websocket.ChatWebSocketManager
import com.ssavice.network.websocket.model.WebSocketChatMessage
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
    private val chatDao: ChatDao,
    private val chatRoomDao: ChatRoomDao,
) {
    fun startObserving() {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            webSocketManager.events.collect { event ->
                handleEvent(event)
            }
        }
    }

    private suspend fun handleEvent(event: WebSocketRxEntity) {
        when(event) {
            is WebSocketRxEntity.TextChat -> {
                getMessage(event)
            }
            is WebSocketRxEntity.ServiceInfoChat -> {
                getMessage(event)
            }
            else -> { }
        }
    }

    private suspend fun getMessage(event: WebSocketChatMessage) {
        Log.d("WebSocketEventHandler", "Handling message: $event")
        val content: String
        val type: String
        when (event) {
            is WebSocketRxEntity.TextChat -> {
                content = event.content
                type = ChatType.TEXT.value
            }

            is WebSocketRxEntity.ServiceInfoChat -> {
                content = event.serviceId.toString()
                type = ChatType.SERVICE.value
            }

            else -> {
                Log.e("WebSocketEventHandler", "Unknown message type: $event")
                return
            }
        }
        chatDao.insertIfContinuous(
            ChatEntity(
                id = event.messageId,
                userId = event.senderId,
                roomId = event.roomId,
                content = content,
                createdAt = event.createdAt,
                type = type
            )
        )

        chatRoomDao.updateRoomLastMessage(
            roomId = event.roomId,
            lastMessage = content,
            lastMessageCreatedAt = event.createdAt,
            lastMessageId = event.messageId
        )
    }
}
