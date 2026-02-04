package com.ssavice.seller_reviews

data class SellerReviewUiState(
    val reviews: List<SellerReviewItemState> = listOf(),
    val hasNext: Boolean = false,
    val nextPage: Int = 0,
    val sellerId: Long = -1L,
    val reviewState: SellerReviewState = SellerReviewState.Initial,
)

data class SellerReviewItemState(
    val index: Int,
    val userName: String,
    val comment: String,
    val serviceName: String,
    val createdAt: String,
    val rate: Int,
)

sealed interface SellerReviewState {
    object Initial : SellerReviewState

    object Loading : SellerReviewState

    object Idle : SellerReviewState

    data class Error(
        val message: Throwable,
    ) : SellerReviewState
}
