package com.ssavice.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.ssavice.chat.navigation.ChatRouteContract
import com.ssavice.chat.repository.ChatRepository
import com.ssavice.model.DateTime
import com.ssavice.model.enums.ChatType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val opponentId: Long? = savedStateHandle[ChatRouteContract.USER_ID]
        private val yourId: Long = 3L

        private val _uiState by lazy {
            val roomId: String? = savedStateHandle[ChatRouteContract.ROOM_ID]
            MutableStateFlow(
                ChattingRoomUiState(
                    chattingRoomState = if (roomId != null) ChattingRoomState.Ready else ChattingRoomState.Pending,
                    roomId = roomId ?: "",
                    serviceIdToSend = savedStateHandle[ChatRouteContract.SERVICE_ID] ?: -1L,
                    sendingService = (savedStateHandle.contains(ChatRouteContract.SERVICE_ID)),
                ),
            )
        }
        val uiState = _uiState.asStateFlow()

        val pagingState by lazy {
            chatRepository
                .getChatMessages(
                    _uiState.value.roomId,
                ).map {
                    var lastTime = DateTime.fromTimeStamp(0)
                    var lastSender = -1L
                    it.map { message ->
                        val time = DateTime.fromTimeStamp(message.createdAt)
                        val result =
                            if (message.type == ChatType.TEXT.value) {
                                ChatMessage.TextMessage(
                                    userId = message.senderId,
                                    you = message.senderId == yourId,
                                    first = lastTime != time || lastSender != message.senderId,
                                    text = message.content,
                                    time = time,
                                    messageId = message.messageId,
                                )
                            } else {
                                collectServiceInfoIfNotExist(message.content.toLongOrNull() ?: -1L)
                                ChatMessage.ServiceMessage(
                                    userId = message.senderId,
                                    you = message.senderId == yourId,
                                    serviceId = message.content.toLongOrNull() ?: 0L,
                                    time = time,
                                    messageId = message.messageId,
                                )
                            }
                        lastTime = time
                        lastSender = message.senderId
                        result
                    }
                }.cachedIn(viewModelScope)
        }

        private fun collectServiceInfoIfNotExist(serviceId: Long) {
            if (serviceId == -1L) return
            if (_uiState.value.serviceInfo.containsKey(serviceId)) return

            viewModelScope.launch(Dispatchers.IO) {
                delay(500)
                _uiState.update {
                    val t = it.serviceInfo.toMutableMap()
                    t[serviceId] =
                        ServiceInfo(
                            name = "Info For Test",
                            thumbnail = "https://picsum.photos/200",
                            basicPrice = 10000,
                            discountPrice = 9000,
                            seller = "Seller",
                        )
                    it.copy(
                        serviceInfo = t,
                    )
                }
            }
        }

        fun requestServiceInfo(serviceId: Long) {
            collectServiceInfoIfNotExist(serviceId)
        }

        fun sendChat(message: String) {
            if (_uiState.value.roomId == "" &&
                opponentId != null &&
                uiState.value.serviceIdToSend != -1L
            ) {
                readyToRedirect(yourId, uiState.value.serviceIdToSend)
                viewModelScope.launch(Dispatchers.IO) {
                    chatRepository.startChat(opponentId, uiState.value.serviceIdToSend, message)
                }
            } else if (_uiState.value.roomId.isNotEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    chatRepository.sendChat(_uiState.value.roomId, message, _uiState.value.roomType)
                }
            }
        }

        private fun readyToRedirect(
            yourId: Long,
            serviceId: Long,
        ) {
            _uiState.update {
                it.copy(
                    sendingService = false,
                    waitingForRedirection = true,
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository
                    .readyForAck(
                        serviceId = serviceId,
                        userId = yourId,
                    ).catch { e -> Log.e("ChattingViewModel", "Error while waiting for ack", e) }
                    .collect { roomId ->
                        roomId.onSuccess { roomId ->
                            redirectChattingRoom(roomId)
                        }
                    }
            }
        }

        private fun redirectChattingRoom(roomId: String) {
            _uiState.update {
                it.copy(
                    roomId = roomId,
                    chattingRoomState = ChattingRoomState.Ready,
                )
            }
        }

        fun updateLastRead(messageId: Int) {
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.setLastReadMessageId(_uiState.value.roomId, messageId)
            }
        }

        fun loadChattingRoomInfo() {
            if (_uiState.value.roomInfoLoadState == ChattingRoomInfoLoadState.Loading) return

            _uiState.update {
                it.copy(roomInfoLoadState = ChattingRoomInfoLoadState.Loading)
            }
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.getRoomInfo(_uiState.value.roomId).onSuccess { info ->
                    _uiState.update {
                        it.copy(
                            roomName = info.name,
                            roomType = info.roomType,
                            roomInfoLoadState = ChattingRoomInfoLoadState.Success,
                            userInfo = mapOf(), // TODO: 따로 받아와서 처리
                        )
                    }
                }
            }
        }
    }
