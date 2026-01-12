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
    val basePrice: Long,
    val category: String,
    val companyId: Long,
    val companyName: String,
    val currentMember: Long,
    val deadline: String,
    val discountRate: Int,
    val discountedPrice: Long,
    val latitude: Int,
    val longitude: Int,
    val maximumMember: Long,
    val minimumMember: Long,
    val region1: String,
    val region2: String,
    val serviceId: Long,
    val status: String,
    val tag: String,
    val thumbnailUrl: String,
    val title: String,
)
