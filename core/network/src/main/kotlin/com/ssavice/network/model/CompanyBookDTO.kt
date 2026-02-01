package com.ssavice.network.model

import com.ssavice.model.Date
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.mapState
import com.ssavice.model.seller.SellerServiceParticipation
import com.ssavice.model.seller.SellerServiceParticipationItem
import com.ssavice.model.user.UserServiceParticipation
import com.ssavice.model.user.UserServiceParticipationItem
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
    val bookStatus: String,
    val isReviewed: Boolean,
    @SerialName("service")
    val serviceInfo: ServiceInfoDTO,
) {
    fun toModel(): SellerServiceParticipationItem =
        with(serviceInfo) {
            SellerServiceParticipationItem(
                id = serviceId,
                thumbnail = thumbnailUrl,
                category = category,
                price = discountedPrice.toInt(),
                name = title,
                sellerName = companyName,
                sellerId = companyId,
                startDate = Date.now(),
                endDate = Date.now(),
                state = ServiceState.mapState(bookStatus),
                isReviewed = isReviewed,
            )
        }
}
