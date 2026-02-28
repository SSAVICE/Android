package com.ssavice.chat

import com.ssavice.model.enums.RoomType

data class ChattingRoomUiState(
    val userInfo: Map<Long, UserInfo> = emptyMap(),
    val roomName: String = "",
    val roomType: RoomType = RoomType.DM,
    val loadState: ChattingRoomLoadState = ChattingRoomLoadState.Initial,
)

sealed interface ChattingRoomLoadState {
    object Initial : ChattingRoomLoadState

    object Loading : ChattingRoomLoadState

    object Success : ChattingRoomLoadState
}

data class UserInfo(
    val name: String,
    val thumbnail: String,
)
