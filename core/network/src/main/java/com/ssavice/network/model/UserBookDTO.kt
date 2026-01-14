package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.Date
import com.ssavice.model.service.ServiceState
import com.ssavice.model.user.UserServiceParticipation
import com.ssavice.model.user.UserServiceParticipationItem
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserBookDTO(
    val content: List<Content>,
    val currentPage: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
) {
    fun toModel(): UserServiceParticipation =
        UserServiceParticipation(
            items = content.map { it.toModel() },
            currentPage = currentPage.toLong(),
            searchCount = totalElements.toInt(),
            hasNext = totalPages > currentPage
        )
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Content(
    val bookStatus: String,
    val isReviewed: Boolean,
    val serviceInfo: ServiceInfoDTO
) {
    fun toModel(): UserServiceParticipationItem = with(serviceInfo) {
        UserServiceParticipationItem(
            id = serviceId,
            thumbnail = thumbnailUrl,
            category = category,
            price = discountedPrice.toInt(),
            name = title,
            sellerName = companyName,
            startDate = Date.now(),
            endDate = Date.now(),
            state = ServiceState.valueOf(status),
            isReviewed = isReviewed,
        )
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ServiceInfoDTO(
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
    val region: RegionDTO,
    val serviceId: Long,
    val status: String,
    val tag: String,
    val thumbnailUrl: String,
    val title: String
)
