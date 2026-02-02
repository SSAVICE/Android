package com.ssavice.user_liked

data class UserLikedUiState(
    val services: List<UserLikedItem> = listOf(),
    val wishServiceScreenStatus: WishServiceState = WishServiceState.Initial,
    val hasNext: Boolean = false,
    val nextPage: Int = 0,
)

sealed interface WishServiceState {
    object Initial : WishServiceState

    object Loading : WishServiceState

    object Loaded : WishServiceState

    data class Error(
        val message: Throwable,
    ) : WishServiceState
}

data class UserLikedItem(
    val index: Int,
    val id: Long,
    val imageUrl: String,
    val sellerName: String,
    val serviceName: String,
    val tags: List<String>,
    val locationInfo: String,
    val deadline: String,
    val price: Int,
    val discountedPrice: Int,
    val participationInfo: String,
    val discountRate: Int,
)
