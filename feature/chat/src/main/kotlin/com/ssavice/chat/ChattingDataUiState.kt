package com.ssavice.chat

import com.ssavice.model.chat.ChattingServiceSummary
import com.ssavice.model.chat.ChattingUserInfo
import com.ssavice.model.enums.RoomType

data class ChattingDataUiState(
    val userInfo: Map<Long, ChattingUserInfo> = emptyMap(),
    val serviceInfo: Map<Long, ChattingServiceSummary> = emptyMap(),
)

data class RoomUiState(
    val yourId: Long,
    val sendingService: Boolean = false,
    val waitingForRedirection: Boolean = false,
    val serviceIdToSend: Long = -1,
    val roomId: String,
    val roomName: String = "",
    val roomType: RoomType = RoomType.DM,
    val chattingRoomState: ChattingRoomState,
    val roomInfoLoadState: ChattingRoomInfoLoadState = ChattingRoomInfoLoadState.Initial,
)

sealed interface ChattingRoomInfoLoadState {
    object Initial : ChattingRoomInfoLoadState

    object Loading : ChattingRoomInfoLoadState

    object Success : ChattingRoomInfoLoadState
}

sealed interface ChattingRoomState {
    object Pending : ChattingRoomState

    object Initial : ChattingRoomState

    object Loading : ChattingRoomState

    object Ready : ChattingRoomState

    object FETCHING_ID : ChattingRoomState
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
