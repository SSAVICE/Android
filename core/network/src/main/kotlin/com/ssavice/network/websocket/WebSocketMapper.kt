package com.ssavice.network.websocket

import com.ssavice.common.DomainFormatter
import com.ssavice.model.enums.RoomType
import com.ssavice.network.websocket.model.WebSocketMessageType
import com.ssavice.network.websocket.model.WebSocketRequest
import com.ssavice.network.websocket.model.WebSocketResponse
import com.ssavice.network.websocket.model.WebSocketRxEntity
import com.ssavice.network.websocket.model.WebSocketTxEntity
import com.ssavice.network.websocket.model.mapType

class WebSocketMapper {
    fun mapWebSocketResponse(response: WebSocketResponse): WebSocketRxEntity {
        return when (WebSocketMessageType.mapType(response.messageType)) {
            WebSocketMessageType.TEXT -> {
                WebSocketRxEntity.TextChat(
                    roomId = response.roomId,
                    senderId = response.sender,
                    content = response.message,
                    createdAt = DomainFormatter.formatTimeToMilliseconds(response.createdAt),
                    messageId = response.messageId,
                )
            }

            WebSocketMessageType.INFO -> {
                WebSocketRxEntity.ServiceInfoChat(
                    roomId = response.roomId,
                    senderId = response.sender,
                    serviceId = response.serviceId,
                    createdAt = DomainFormatter.formatTimeToMilliseconds(response.createdAt),
                    messageId = response.messageId,
                )
            }

            else -> {
                return WebSocketRxEntity.Ignore
            }
        }
    }

    fun mapWebSocketRequest(request: WebSocketTxEntity): WebSocketRequest =
        when (request) {
            is WebSocketTxEntity.SendTextChat -> {
                WebSocketRequest(
                    messageType = WebSocketMessageType.TEXT.value,
                    roomType = request.roomType.value,
                    roomId = request.roomId,
                    receiver = 0,
                    message = request.content,
                    serviceId = 0,
                    readMsgId = 0,
                )
            }

            is WebSocketTxEntity.SendRead -> {
                WebSocketRequest(
                    messageType = WebSocketMessageType.READ.value,
                    roomType = request.roomType.value,
                    roomId = request.roomId,
                    receiver = 0,
                    message = "",
                    serviceId = 0,
                    readMsgId = request.lastMessageId,
                )
            }

            is WebSocketTxEntity.SendServiceInfo -> {
                WebSocketRequest(
                    messageType = WebSocketMessageType.INFO.value,
                    roomType = request.roomType.value,
                    roomId = request.roomId,
                    receiver = 0,
                    message = "",
                    serviceId = request.serviceId,
                    readMsgId = 0,
                )
            }

            is WebSocketTxEntity.NewTextDM -> {
                WebSocketRequest(
                    messageType = WebSocketMessageType.TEXT.value,
                    roomType = RoomType.DM.value,
                    roomId = "",
                    receiver = request.receiverId,
                    message = request.content,
                    serviceId = 0,
                    readMsgId = 0,
                )
            }

            is WebSocketTxEntity.NewServiceInfoDM -> {
                WebSocketRequest(
                    messageType = WebSocketMessageType.INFO.value,
                    roomType = RoomType.DM.value,
                    roomId = "",
                    receiver = request.receiverId,
                    message = "",
                    serviceId = request.serviceId,
                    readMsgId = 0,
                )
            }
        }
}
