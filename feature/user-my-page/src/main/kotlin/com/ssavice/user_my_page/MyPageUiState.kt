package com.ssavice.user_my_page

data class MyPageUiState(
    val profile: ProfileState? = null,
    val participation: ParticipationState? = null,
    val profileState: MyPageState = MyPageState.Loading,
    val participationState: MyPageState = MyPageState.Loading,
)

data class ProfileState(
    val name: String,
    val locationInfo: String,
    val description: String,
    val createdAt: String,
    val profileUrl: String
)

data class ParticipationState(
    val onProgress: Int,
    val done: Int,
    val total: Int
)

sealed interface MyPageState {
    object Loading : MyPageState

    object Done : MyPageState

    data class Error(
        val message: Throwable,
    ) : MyPageState
}
