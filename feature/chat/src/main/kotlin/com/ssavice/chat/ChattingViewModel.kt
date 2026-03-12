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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    val chattingUiState by lazy {
        chatRepository
            .getChatServiceSummaryMap()
            .combine(
                chatRepository.getUserInfoMap(),
            ) { serviceSummary, userInfo ->
                ChattingDataUiState(
                    userInfo = userInfo,
                    serviceInfo = serviceSummary,
                )
            }.stateIn(
                scope = viewModelScope,
                started =
                    kotlinx.coroutines.flow.SharingStarted
                        .WhileSubscribed(5000L),
                initialValue = ChattingDataUiState(),
            )
    }
    private val _roomUiState by lazy {
        val roomId: String? = savedStateHandle[ChatRouteContract.ROOM_ID]
        MutableStateFlow(
            RoomUiState(
                chattingRoomState = ChattingRoomState.Initial,
                roomId = roomId ?: "",
                serviceIdToSend = savedStateHandle[ChatRouteContract.SERVICE_ID] ?: -1L,
                sendingService = (savedStateHandle.contains(ChatRouteContract.SERVICE_ID)),
                yourId = -1L,
            ),
        )
    }

    val roomUiState = _roomUiState.asStateFlow()

    val pagingState by lazy {
        chatRepository
            .getChatMessages(
                _roomUiState.value.roomId,
            ).map {
                var lastTime = DateTime.fromTimeStamp(0)
                var lastSender = -1L
                it.map { message ->
                    val time = DateTime.fromTimeStamp(message.createdAt)
                    val result =
                        if (message.type == ChatType.TEXT.value) {
                            ChatMessage.TextMessage(
                                userId = message.senderId,
                                you = message.senderId == roomUiState.value.yourId,
                                first = lastTime != time || lastSender != message.senderId,
                                text = message.content,
                                time = time,
                                messageId = message.messageId,
                            )
                        } else {
                            Log.d(TAG, message.toString())
                            message.content.toLongOrNull()?.let { id ->
                                viewModelScope.launch(Dispatchers.IO) {
                                    chatRepository.updateServiceSummaryIfNeed(id)
                                }
                            }
                            ChatMessage.ServiceMessage(
                                userId = message.senderId,
                                you = message.senderId == roomUiState.value.yourId,
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

    fun sendChat(message: String) {
        if (_roomUiState.value.roomId == "" &&
            opponentId != null &&
            _roomUiState.value.serviceIdToSend != -1L
        ) {
            readyToRedirect(roomUiState.value.yourId, roomUiState.value.serviceIdToSend)
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.startChat(opponentId, _roomUiState.value.serviceIdToSend, message)
            }
        } else if (_roomUiState.value.roomId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.sendChat(
                    _roomUiState.value.roomId,
                    message,
                    _roomUiState.value.roomType,
                )
            }
        }
    }

    private fun readyToRedirect(
        yourId: Long,
        serviceId: Long,
    ) {
        Log.d(TAG, "ready to redirect. Your id = $yourId, serviceId = $serviceId")
        _roomUiState.update {
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
                ).catch { e -> Log.e(TAG, "Error while waiting for ack", e) }
                .collect { roomId ->
                    Log.d(TAG, "received Ack. redirecting")
                    roomId.onSuccess { roomId ->
                        redirectChattingRoom(roomId)
                    }
                }
        }
    }

    private fun redirectChattingRoom(roomId: String) {
        _roomUiState.update {
            it.copy(
                roomId = roomId,
            )
        }
        loadChattingRoomInfo()
    }

    fun updateLastRead(messageId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.setLastReadMessageId(_roomUiState.value.roomId, messageId)
        }
    }

    /**
     * **Step 1: 사용자 ID 조회 단계**
     *
     * 채팅 화면 진입 시 가장 먼저 호출되어야 하는 함수입니다.
     * 성공 시 **Step 2: Init Room**으로 진행합니다.
     */
    fun getYourId() {
        if (_roomUiState.value.yourId != -1L ||
            _roomUiState.value.chattingRoomState != ChattingRoomState.Initial
        ) {
            return
        }
        _roomUiState.update {
            it.copy(
                chattingRoomState = ChattingRoomState.FetchingId,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            var isSuccess = false
            var retryCount = 0
            val maxRetries = 5

            while (retryCount < 5 && !isSuccess) {
                chatRepository
                    .getMyId()
                    .onSuccess { id ->
                        _roomUiState.update {
                            it.copy(yourId = id) // it.yourId가 아닌 받아온 id를 넣어야 함
                        }
                        initRoom()
                        isSuccess = true
                    }.onFailure { error ->
                        retryCount++
                        Log.e(TAG, "getMyId 실패 ($retryCount/$maxRetries): ${error.message}")

                        if (retryCount < maxRetries) {
                            kotlinx.coroutines.delay(1000L * retryCount)
                        } else {
                            // 최종 실패 시 처리 (예: 에러 상태 업데이트)
                            Log.e(TAG, "최대 재시도 횟수 초과")
                        }
                    }
            }
        }
    }


    /**
     * **Step 2: 채팅방 초기화 및 분기 처리**
     *
     * 내 ID가 확보된 후, 기존 채팅방 진입인지 신규 채팅 시도(방 없음)인지 판단합니다.
     *
     * 신규 채팅일 시, **ChattingRoomState**를 **Pending**으로 초기화 합니다.
     *
     * 채팅방 정보가 있을 시, **Load Chatting Room Info**로 진행합니다
     */
    fun initRoom() {
        val roomId: String? = savedStateHandle[ChatRouteContract.ROOM_ID]
        if (roomId == null) {
            savedStateHandle.get<Long>(ChatRouteContract.SERVICE_ID)?.let { id ->
                viewModelScope.launch {
                    chatRepository.updateServiceSummaryIfNeed(id)
                }
            }
            _roomUiState.update {
                it.copy(
                    chattingRoomState = ChattingRoomState.Pending,
                    roomId = "",
                    serviceIdToSend = savedStateHandle[ChatRouteContract.SERVICE_ID] ?: -1L,
                    sendingService = (savedStateHandle.contains(ChatRouteContract.SERVICE_ID)),
                )
            }
        } else {
            _roomUiState.update {
                it.copy(
                    roomId = roomId,
                    serviceIdToSend = -1L,
                    sendingService = false,
                )
            }
            loadChattingRoomInfo()
        }
    }

    /**
     * **Step 3: 채팅방 상세 정보 로드**
     *
     * 존재하는 roomId를 기반으로 서버에서 방 이름, 참여자 정보 등을 가져옵니다.
     *
     * 로드 완료 시 ChattingRoomState는 최종적으로 **Ready** 상태가 됩니다.
     */
    fun loadChattingRoomInfo() {
        if (_roomUiState.value.roomInfoLoadState == ChattingRoomInfoLoadState.Loading) return

        _roomUiState.update {
            it.copy(
                roomInfoLoadState = ChattingRoomInfoLoadState.Loading,
                chattingRoomState = ChattingRoomState.Loading,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.getRoomInfo(_roomUiState.value.roomId).onSuccess { info ->
                _roomUiState.update {
                    it.copy(
                        roomName = info.name,
                        roomType = info.roomType,
                        roomInfoLoadState = ChattingRoomInfoLoadState.Success,
                        chattingRoomState = ChattingRoomState.Ready,
                        participantIds = info.participantIds
                    )
                }
                chatRepository.updateUserInfoIfNeed(
                    info.participantIds,
                )
            }
        }
    }

    companion object {
        const val TAG = "ChattingViewModel"
    }
}
