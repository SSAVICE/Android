package com.ssavice.network.model

data class GetUserServiceParticipationDTO(
    val content: List<Content>,
    val currentPage: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)

data class Content(
    val bookStatus: String,
    val isReviewed: Boolean,
    val serviceInfo: ServiceInfo,
)

data class ServiceInfo(
    val serviceId: Long,
    val basePrice: Long,
    val category: String,
    val companyId: Long,
    val companyName: String,
    val currentMember: Long,
    val deadline: String,
    val discountRate: Int,
    val discountedPrice: Long,
    val maximumMember: Long,
    val minimumMember: Long,
    val status: String,
    val tag: String,
    val thumbnailUrl: String,
    val title: String,
    val region: RegionDTO
)
