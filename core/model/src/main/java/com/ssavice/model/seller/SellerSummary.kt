package com.ssavice.model.seller

import com.ssavice.model.Review

data class SellerSummary(
    val companyId: Long,
    val companyName: String,
    val address: String,
    val description: String,
    val phoneNumber: String,
    val companyImageUrl: String?,
    val companyRate: Double,
    val rateCount: Int,
    val review: List<Review>
)
