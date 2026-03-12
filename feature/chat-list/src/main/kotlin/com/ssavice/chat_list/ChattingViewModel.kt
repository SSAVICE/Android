package com.ssavice.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.chat.repository.ChatRepository
import com.ssavice.model.DateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
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
                .distinctUntilChanged()
                .map {
                    it
                        .sortedByDescending { room -> room.lastUpdate }
                        .map { room ->
                            val lastUpdate = DateTime.fromTimeStamp(room.lastUpdate)
                            ChattingRoomItem(
                                name = room.name,
                                serviceId = null,
                                serviceName = "",
                                lastUpdate = lastUpdate,
                                unreadCount = room.unreadCount,
                                roomId = room.roomId,
                                lastMessage = room.lastMessage,
                                lastUpdateString = lastUpdate.dateToSimpleString(),
                            )
                        }
                }
                .flowOn(Dispatchers.Default)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList(),
                )
        }

        fun onRefresh() {
            chatRepository.refreshRoomList()
        }
    }
