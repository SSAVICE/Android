package com.ssavice.network.model

import com.ssavice.model.Date
import com.ssavice.model.Review
import com.ssavice.model.seller.SellerSummary
import kotlinx.serialization.Serializable

@Serializable
data class GetCompanySummaryDTO(
    val companyId: Long,
    val companyName: String,
    val address: String,
    val description: String,
    val phoneNumber: String,
    val companyImageUrl: String?,
    val companyRate: Double,
    val rateCount: Long,
    val review: List<ReviewDTO>,
) {
    fun toModel(): SellerSummary =
        SellerSummary(
            companyId = companyId,
            companyName = companyName,
            address = address,
            description = description,
            phoneNumber = phoneNumber,
            companyImageUrl = companyImageUrl,
            companyRate = companyRate,
            rateCount = rateCount.toInt(),
            review =
                review.map {
                    it.toModel()
                },
        )
}

@Serializable
data class ReviewDTO(
    val userName: String,
    val comment: String,
    val serviceName: String,
    val createdAt: String,
    val rate: Integer,
) {
    fun toModel(): Review =
        Review(
            userName = userName,
            comment = comment,
            rating = rate.toInt(),
            createdAt = Date.parse(createdAt),
            serviceName = serviceName,
        )
}
