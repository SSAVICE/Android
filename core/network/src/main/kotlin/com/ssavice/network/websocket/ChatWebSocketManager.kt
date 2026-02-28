package com.ssavice.network.websocket

import android.util.Log
import com.ssavice.core.network.BuildConfig
import com.ssavice.network.websocket.model.WebSocketResponse
import com.ssavice.network.websocket.model.WebSocketRxEntity
import com.ssavice.network.websocket.model.WebSocketTxEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class ChatWebSocketManager @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val webSocketDtoMapper: WebSocketMapper,
    private val json: Json
) {
    private var webSocket: WebSocket? = null
    private val _events = MutableSharedFlow<WebSocketRxEntity>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    fun connect() {
        val request = Request.Builder()
            .url(BuildConfig.WEBSOCKET_URL)
            .build()
        webSocket = okHttpClient.newWebSocket(request, object: WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val dto = json.decodeFromString<WebSocketResponse>(text)
                    val entity = webSocketDtoMapper.mapWebSocketResponse(dto)
                    _events.tryEmit(entity)
                } catch (e: Exception) {
                    Log.e("ChatWebSocketManager", "Failed to parse websocket message: $text", e)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // 에러 처리 로직
            }
        })
    }

    fun sendMessage(data: WebSocketTxEntity) {
        try {
            val dto = webSocketDtoMapper.mapWebSocketRequest(data)
            val json = json.encodeToJsonElement(dto)
            webSocket?.send(json.toString())
        } catch(e: Exception) {
            Log.e("ChatWebSocketManager", "Failed to send websocket message: $data")
        }
    }

    fun close() {
        webSocket?.close(1000, "Normal Closure")
        webSocket = null
    }
}
