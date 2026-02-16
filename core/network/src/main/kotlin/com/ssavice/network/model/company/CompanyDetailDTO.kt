package com.ssavice.network.model.company

import com.ssavice.model.Date
import com.ssavice.model.Region
import com.ssavice.model.Review
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.mapState
import com.ssavice.model.seller.SellerDetail
import com.ssavice.model.service.ServiceSummary
import com.ssavice.network.model.RegionDTO
import kotlinx.serialization.Serializable

@Serializable
data class CompanyDetailDTO(
    val address: String,
    val businessNumber: String,
    val companyId: Long,
    val companyName: String,
    val description: String,
    val detail: String?,
    val detailAddress: String,
    val imageUrl: String?,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
    val postCode: String,
    val review: List<ServiceDetailReviewItemDTO>,
    val service: List<ServiceDetailServiceItemDTO>,
) {
    fun toModel(): SellerDetail =
        SellerDetail(
            id = companyId,
            thumbnailUrl = imageUrl ?: "",
            sellerName = companyName,
            address = address,
            detailAddress = detailAddress,
            imageUrls = listOf(),
            region =
                Region(
                    latitude = latitude,
                    longitude = longitude,
                    region1 = "",
                    region2 = "",
                ),
            description = description,
            detail = detail ?: "",
            phoneNumber = phoneNumber,
            serviceItems = service.map { it.toModel() },
            reviewItems = review.map { it.toModel() },
            ownerName = "",
            ownerPhoneNumber = "",
            businessNumber = businessNumber,
        )
}

@Serializable
data class ServiceDetailReviewItemDTO(
    val comment: String,
    val createdAt: String,
    val rating: Int,
    val serviceName: String,
    val userName: String,
) {
    fun toModel(): Review =
        Review(
            userName = userName,
            comment = comment,
            serviceName = serviceName,
            createdAt = Date.parse(createdAt),
            rating = rating,
        )
}

@Serializable
data class ServiceDetailServiceItemDTO(
    val basePrice: Long,
    val category: String,
    val currentMember: Long,
    val deadline: String,
    val description: String,
    val discountRate: Int,
    val discountedPrice: Long,
    val endDate: String,
    val maximumMember: Long,
    val minimumMember: Long,
    val region: RegionDTO,
    val serviceId: Long,
    val startDate: String,
    val status: String,
    val tag: String,
    val thumbnailUrl: String?,
    val title: String,
) {
    fun toModel(): ServiceSummary =
        ServiceSummary(
            name = title,
            id = serviceId,
            image = thumbnailUrl ?: "",
            category = category,
            minimumMember = minimumMember.toInt(),
            currentMember = currentMember.toInt(),
            basePrice = basePrice,
            discountRatio = discountRate.toDouble(),
            discountedPrice = discountedPrice,
            deadLine = Date.parse(deadline),
            startDate = Date.parse(startDate),
            endDate = Date.parse(endDate),
            serviceTag = tag,
            state = ServiceState.mapState(status),
        )
}
