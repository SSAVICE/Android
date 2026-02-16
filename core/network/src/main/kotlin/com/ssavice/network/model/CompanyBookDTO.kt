package com.ssavice.network.model

import com.ssavice.model.Date
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.mapState
import com.ssavice.model.seller.SellerServiceParticipation
import com.ssavice.model.seller.SellerServiceParticipationItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyBookDTO(
    val content: List<CompanyContent>,
    val currentPage: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
) {
    fun toModel(): SellerServiceParticipation =
        SellerServiceParticipation(
            items = content.map { it.toModel() },
            currentPage = currentPage.toLong(),
            searchCount = totalElements.toInt(),
            hasNext = totalPages > currentPage + 1,
        )
}

@Serializable
data class CompanyContent(
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
    val thumbnailUrl: String,
    val title: String,
) {
    fun toModel(): SellerServiceParticipationItem =
        SellerServiceParticipationItem(
            id = serviceId,
            thumbnail = thumbnailUrl,
            category = category,
            price = discountedPrice.toInt(),
            name = title,
            startDate = Date.parse(startDate),
            endDate = Date.parse(endDate),
            state = ServiceState.mapState(status),
            currentMemberCount = currentMember.toInt(),
            minimumMemberCount = minimumMember.toInt(),
            maximumMemberCount = maximumMember.toInt(),
        )
}
