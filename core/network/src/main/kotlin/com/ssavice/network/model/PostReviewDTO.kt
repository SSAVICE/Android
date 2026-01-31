package com.ssavice.network.model

import com.ssavice.model.service.ReviewForm
import kotlinx.serialization.Serializable

@Serializable
data class PostReviewDTO(
    val companyId: Long,
    val serviceId: Long,
    val rating: Int,
    val comment: String
) {
    companion object {
        fun fromModel(review: ReviewForm): PostReviewDTO {
            return PostReviewDTO(
                companyId = review.sellerId,
                serviceId = review.serviceId,
                rating = review.rating,
                comment = review.content
            )
        }
    }
}
