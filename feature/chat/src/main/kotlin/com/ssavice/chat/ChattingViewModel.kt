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
    private val roomId: String = savedStateHandle[ChatRouteContract.ROOM_ID] ?: ""
    private val opponentId: Long? = savedStateHandle[ChatRouteContract.USER_ID]


    val pagingState by lazy {
        if(roomId.isEmpty()) null
        else {
            chatRepository
                .getChatMessages(
                    roomId,
                ).cachedIn(viewModelScope)
        }
    }

    private val _roomInfoState =
        MutableStateFlow(
            ChattingRoomUiState(),
        )

    val roomInfoState = _roomInfoState.asStateFlow()

    fun sendChat(message: String) {
        if(roomId.isEmpty() && opponentId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.startChat(opponentId, message)
            }

        }
        else if(roomId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                chatRepository.sendChat(roomId, message, _roomInfoState.value.roomType)
            }
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
                        userInfo = mapOf() // TODO: 따로 받아와서 처리
                    )
                }
            }
        }
    }
}
