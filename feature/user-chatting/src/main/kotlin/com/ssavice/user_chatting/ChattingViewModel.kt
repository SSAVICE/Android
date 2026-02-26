package com.ssavice.user_chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.chat.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val chattingRoomState by lazy {
        chatRepository.getRoomList().map {
            it.sortedByDescending { room -> room.lastUpdate }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }
}
