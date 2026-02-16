package com.ssavice.network.model.review

import com.ssavice.model.seller.SellerReviews
import com.ssavice.network.model.company.ReviewDTO
import kotlinx.serialization.Serializable

@Serializable
data class GetCompanyReviewDTO(
    val content: List<ReviewDTO>,
    val currentPage: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
) {
    fun toModel(): SellerReviews =
        SellerReviews(
            currentPage = currentPage.toLong(),
            searchCount = size,
            hasNext = currentPage < totalPages,
            reviews = content.map { it.toModel() },
        )
}
