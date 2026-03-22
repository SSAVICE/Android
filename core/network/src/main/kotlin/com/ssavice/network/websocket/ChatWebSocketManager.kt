package com.ssavice.network.websocket

import android.util.Log
import com.ssavice.core.network.BuildConfig
import com.ssavice.network.NetworkEvent
import com.ssavice.network.NetworkEventManager
import com.ssavice.network.websocket.model.WebSocketResponse
import com.ssavice.network.websocket.model.WebSocketRxEntity
import com.ssavice.network.websocket.model.WebSocketTxEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class ChatWebSocketManager
@Inject
constructor(
    private val okHttpClient: OkHttpClient,
    private val webSocketDtoMapper: WebSocketMapper,
    private val json: Json,
    private val networkEventManager: NetworkEventManager?,
) {
    private var webSocket: WebSocket? = null
    private val _events = MutableSharedFlow<WebSocketRxEntity>(extraBufferCapacity = 64)
    private var reconnectCount = 0
    val events = _events.asSharedFlow()
    private var intentionalClose = false
    private val externalScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var pingJob: Job? = null

    private fun tryConnect(): ChatWebSocketManager {
        intentionalClose = false
        try {
            val request =
                Request
                    .Builder()
                    .url(BuildConfig.WEBSOCKET_URL + "/ws/chat")
                    .build()

            webSocket =
                okHttpClient.newWebSocket(
                    request,
                    object : WebSocketListener() {
                        override fun onOpen(
                            webSocket: WebSocket,
                            response: Response,
                        ) {
                            Log.d(TAG, "Websocket connected")
                            reconnectCount = 0
                            startPingLoop()
                            super.onOpen(webSocket, response)
                        }

                        override fun onMessage(
                            webSocket: WebSocket,
                            text: String,
                        ) {
                            try {
                                if (text.length == 4 && text.lowercase() == "pong") {
                                Log.i(TAG, "received pong message")
                                return
                            }
                                val dto = json.decodeFromString<WebSocketResponse>(text)
                                Log.d(TAG, "Got websocket message: $dto.")
                                val entity = webSocketDtoMapper.mapWebSocketResponse(dto)
                                _events.tryEmit(entity)
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "Failed to parse websocket message: $text",
                                    e,
                                )
                            }
                        }

                        override fun onFailure(
                            ws: WebSocket,
                            t: Throwable,
                            response: Response?,
                        ) {
                            Log.e(TAG, "Error connecting to websocket", t)
                            stopPingLoop()
                            webSocket = null
                            if (!intentionalClose) {
                                reconnect()
                            }
                        }

                        override fun onClosed(
                            ws: WebSocket,
                            code: Int,
                            reason: String,
                        ) {
                            stopPingLoop()
                            webSocket = null
                            Log.d(TAG, "Websocket closed: $code, $reason")
                        }
                    },
                )
        } catch (
            e: Exception,
        ) {
            Log.e("ChatWebSocketManager", "Failed to connect to websocket", e)
            webSocket?.close(1006, "Error connecting to websocket")
            webSocket = null
            reconnect()
        }
        return this
    }

    fun connect(): ChatWebSocketManager {
        if (webSocket != null) {
            Log.w(TAG, "Client already running")
            return this
        }
        reconnectCount = 0

        return tryConnect()
    }

    private fun startPingLoop() {
        pingJob?.cancel() // 기존 실행 중인 잡이 있다면 취소
        pingJob = externalScope.launch {
            while (isActive) { // 코루틴이 활성화된 동안 반복
                delay(30000L) // 30초 대기
                if (webSocket != null) {
                    sendPing()
                } else {
                    break // 웹소켓이 없으면 루프 탈출
                }
            }
        }
    }

    private fun stopPingLoop() {
        pingJob?.cancel()
        pingJob = null
    }

    private fun sendPing() {
        Log.d(TAG, "Sending ping message...")
        val success = webSocket?.send("ping") ?: false
        if (!success) {
            Log.e(TAG, "Failed to send ping")
        }
    }
    fun sendMessage(data: WebSocketTxEntity) {
        try {
            val dto = webSocketDtoMapper.mapWebSocketRequest(data)
            val json = json.encodeToJsonElement(dto)
            Log.d(TAG, "Sending websocket message: $json.")
            webSocket?.send(json.toString())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send websocket message: $data")
        }
    }

    fun close() {
        intentionalClose = true
        stopPingLoop()
        webSocket?.close(1000, "Normal Closure")
        webSocket = null
    }

    private fun reconnect() {
        reconnectCount++
        stopPingLoop()
        if (reconnectCount >= MAX_RECONNECT) {
            sendWebsocketNotAvailableEvent()
            return
        }

        Log.d(TAG, "Attempting to reconnect in 3 seconds... (try: $reconnectCount)")
        // 3초 후 재연결 시도 (실제 앱에서는 딜레이를 늘리는 방식 권장)
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            if (webSocket == null) {
                tryConnect()
            }
        }, 3000)
    }

    private fun sendWebsocketNotAvailableEvent() {
        networkEventManager?.tryEmit(NetworkEvent.ChatServerUnavailable)
        _events.tryEmit(WebSocketRxEntity.WebSocketError(Throwable(), "웹소켓 연결이 원활하지 않습니다."))
    }

    companion object {
        fun builder(): WebSocketBuilder = WebSocketBuilder()

        const val MAX_RECONNECT = 5

        class WebSocketBuilder {
            private lateinit var okHttpClient: OkHttpClient
            private lateinit var webSocketDtoMapper: WebSocketMapper
            private lateinit var json: Json
            private var networkEventManager: NetworkEventManager? = null

            fun addClient(client: OkHttpClient): WebSocketBuilder {
                okHttpClient = client
                return this
            }

            fun addMapper(mapper: WebSocketMapper): WebSocketBuilder {
                webSocketDtoMapper = mapper
                return this
            }

            fun addJson(json: Json): WebSocketBuilder {
                this.json = json
                return this
            }

            fun addNetworkEventManager(networkEventManager: NetworkEventManager): WebSocketBuilder {
                this.networkEventManager = networkEventManager
                return this
            }

            fun build(): ChatWebSocketManager {
                val manager = ChatWebSocketManager(
                    okHttpClient,
                    webSocketDtoMapper,
                    json,
                    networkEventManager
                )
                return manager
            }
        }

        private const val TAG = "ChatWebSocketManager"
    }
}
