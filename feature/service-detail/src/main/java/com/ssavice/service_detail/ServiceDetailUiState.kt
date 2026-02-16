package com.ssavice.service_detail

import com.ssavice.service_detail.ui.seller.ParticipantUiModel

sealed interface InfoState {
    object Initial : InfoState

    object Waiting : InfoState

    object Loading : InfoState

    object Done : InfoState

    data class Error(
        val message: Throwable,
    ) : InfoState
}

data class ServiceDetailUiState(
    val serviceInfoState: InfoState = InfoState.Initial,
    val sellerInfoState: InfoState = InfoState.Initial,
    val applyInfoState: InfoState = InfoState.Waiting,
    val serviceLikeState: InfoState = InfoState.Waiting,
    val showUserInfo: Boolean = true,
    val showSellerInfo: Boolean = false,
    val service: ServiceDetail? = null,
    val seller: SellerSummary? = null,
    val accountInfo: SellerAccountInfo? = null,
)

data class ServiceDetail(
    val deadLine: String,
    val startDate: String,
    val endDate: String,
    val imageUrls: List<String>,
    val basePrice: Int,
    val discountedPrice: Int,
    val discountRatio: Int,
    val participantInfo: String,
    val id: Long,
    val companyId: Long,
    val category: String,
    val name: String,
    val address: String,
    val description: String,
    val longitude: Double,
    val latitude: Double,
    val liked: Boolean,
    val applied: Boolean,
    val tags: List<String>,
)

data class SellerSummary(
    val id: Long,
    val name: String,
    val address: String,
    val description: String,
    val phoneNumber: String,
    val imageUrl: String,
    val rate: Double,
    val rateCount: Int,
    val reviews: List<Review>,
)

data class Review(
    val userName: String,
    val content: String,
    val rating: Int,
    val createdAt: String,
    val serviceName: String,
)

data class SellerAccountInfo(
    val expectedRevenue: Long,
    val participantCount: Int,
    val pricePerPerson: Long,
    val lastNotice: String?,
    val noticeDate: String?,
    val participants: List<ParticipantUiModel>,
)
