package com.ssavice.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ssavice.chat.navigation.ChatRouteContract
import com.ssavice.chat.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        private val roomId: Long = savedStateHandle[ChatRouteContract.ROOM_ID] ?: 0

        val pagingState by lazy {
            chatRepository
                .getChatMessages(
                    roomId,
                ).cachedIn(viewModelScope)
        }

        private val _roomInfoState =
            MutableStateFlow<ChattingRoomUiState>(
                ChattingRoomUiState(),
            )

        val roomInfoState = _roomInfoState.asStateFlow()

        fun sendChat(message: String) {
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.sendChat(roomId, message)
            }
        }

        fun updateLastRead(messageId: Int) {
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.setLastReadMessageId(roomId, messageId)
            }
        }

        fun loadChattingRoomInfo() {
            if (_roomInfoState.value.loadState == ChattingRoomLoadState.Loading) return

            _roomInfoState.update {
                it.copy(loadState = ChattingRoomLoadState.Loading)
            }
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.getRoomInfo(roomId).onSuccess { info ->
                    _roomInfoState.update {
                        it.copy(
                            roomName = info.name,
                            roomType = info.roomType,
                            loadState = ChattingRoomLoadState.Success,
                            userInfo =
                                info.participants.associate { t ->
                                    t.userId to
                                        UserInfo(
                                            name = t.name,
                                            thumbnail = t.thumbnail,
                                        )
                                },
                        )
                    }
                }
            }
        }
    }
