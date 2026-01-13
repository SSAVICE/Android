package com.ssavice.user_my_page

data class MyPageUiState(
    val profile: ProfileState? = null,
    val participation: ParticipationState? = null,
    val profileState: MyPageState = MyPageState.Waiting,
    val participationState: MyPageState = MyPageState.Waiting,
)

data class ProfileState(
    val name: String,
    val locationInfo: String,
    val description: String,
    val createdAt: String,
    val profileUrl: String,
    val email: String,
    val phoneNumber: String,
)

data class ParticipationState(
    val onProgress: Int,
    val done: Int,
    val total: Int,
)

sealed interface MyPageState {
    object Waiting : MyPageState

    object Loading : MyPageState

    object Done : MyPageState

    data class Error(
        val message: Throwable,
    ) : MyPageState
}
