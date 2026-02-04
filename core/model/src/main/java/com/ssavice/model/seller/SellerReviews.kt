package com.ssavice.model.seller

import com.ssavice.model.Review

data class SellerReviews(
    val currentPage: Long,
    val searchCount: Int,
    val hasNext: Boolean,
    val reviews: List<Review>
)
