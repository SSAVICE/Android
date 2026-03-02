package com.ssavice.chat

import com.ssavice.model.enums.RoomType

data class ChattingRoomUiState(
    val userInfo: Map<Long, UserInfo> = emptyMap(),
    val serviceInfo: Map<Long, ServiceInfo> = emptyMap(),
    val roomId: String,
    val roomName: String = "",
    val roomType: RoomType = RoomType.DM,
    val roomInfoLoadState: ChattingRoomInfoLoadState = ChattingRoomInfoLoadState.Initial,
    val chattingRoomState: ChattingRoomState,
    val sendingService: Boolean = false,
    val waitingForRedirection: Boolean = false,
    val serviceIdToSend: Long = -1,
)

sealed interface ChattingRoomInfoLoadState {
    object Initial : ChattingRoomInfoLoadState

    object Loading : ChattingRoomInfoLoadState

    object Success : ChattingRoomInfoLoadState
}

sealed interface ChattingRoomState {
    object Pending : ChattingRoomState

    object Initial : ChattingRoomState

    object Ready : ChattingRoomState
}

data class UserInfo(
    val name: String,
    val thumbnail: String,
)

data class ServiceInfo(
    val name: String,
    val thumbnail: String,
    val basicPrice: Int,
    val discountPrice: Int,
    val seller: String,
)
