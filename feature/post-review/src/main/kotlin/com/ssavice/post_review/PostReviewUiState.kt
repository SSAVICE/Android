package com.ssavice.post_review

data class PostReviewUiState(
    val serviceName: String,
    val serviceId: Long,
    val serviceThumbnailUrl: String,
    val reviewPostState: ReviewPostState = ReviewPostState.Idle
)

sealed interface ReviewPostState {
    object Initial: ReviewPostState
    object Idle : ReviewPostState
    object Loading: ReviewPostState
    object Success: ReviewPostState
    data class Failure(val message: Throwable): ReviewPostState
}
