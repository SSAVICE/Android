package com.ssavice.user_chatting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.chat.repository.ChatRepository
import com.ssavice.model.DateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) : ViewModel() {
        val chattingRoomState by lazy {
            chatRepository
                .getRoomList()
                .map {
                    it
                        .sortedByDescending { room -> room.lastUpdate }
                        .map { room ->
                            ChattingRoomItem(
                                name = room.name,
                                serviceId = null,
                                serviceName = "",
                                lastUpdate = DateTime.fromTimeStamp(room.lastUpdate),
                                unreadCount = room.unreadCount,
                                roomId = room.roomId,
                                lastMessage = room.lastMessage,
                            )
                        }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList(),
                )
        }
    }
