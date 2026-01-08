package com.ssavice.user_my_service

data class MyServiceUiState(
    val services: List<MyServiceItemUiState> = listOf(),
    val myServiceScreenStatus: MyServiceState = MyServiceState.Loading,
    val hasNext: Boolean = false,
    val nextId: Long,
)

data class MyServiceItemUiState(
    val index: Int,
    val id: Long,
    val title: String,
    val price: String,
    val thumbnailUrl: String,
    val sellerName: String,
    val duration: String,
    val cancellable: Boolean,
    val reviewable: Boolean,
)

sealed interface MyServiceState {
    object Loading : MyServiceState
    object Loaded : MyServiceState
    data class Error(val message: String) : MyServiceState
}
