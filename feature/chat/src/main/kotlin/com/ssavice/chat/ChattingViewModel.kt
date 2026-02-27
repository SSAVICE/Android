package com.ssavice.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ssavice.chat.navigation.ChatRouteContract
import com.ssavice.chat.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val roomId: Long = savedStateHandle[ChatRouteContract.ROOM_ID] ?: 0

    val pagingState by lazy {
        chatRepository.getChatMessages(
            roomId
        ).cachedIn(viewModelScope)
    }

    fun sendChat(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendChat(roomId, message)
        }
    }

    fun updateLastRead(messageId: Int) {
        viewModelScope.launch {
            chatRepository.setLastReadMessageId(roomId, messageId)
        }
    }
}
