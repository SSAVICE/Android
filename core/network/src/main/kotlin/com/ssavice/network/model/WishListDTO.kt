package com.ssavice.network.model

import com.ssavice.model.Date
import com.ssavice.model.service.UserWishListItem
import com.ssavice.model.service.WishList
import kotlinx.serialization.Serializable

@Serializable
data class WishListDTO(
    val content: List<WishContentDTO>,
    val currentPage: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
) {
    fun toModel(): WishList =
        WishList(
            items = content.map { it.toModel() },
            currentPage = currentPage.toLong(),
            searchCount = totalElements.toInt(),
            hasNext = totalPages > currentPage + 1,
        )
}

@Serializable
data class WishContentDTO(
    val basePrice: Long,
    val category: String,
    val companyId: Long,
    val companyName: String,
    val currentMember: Long,
    val deadline: String,
    val discountRatio: Int,
    val discountedPrice: Long,
    val maximumMember: Long,
    val minimumMember: Long,
    val region: RegionDTO,
    val serviceId: Long,
    val tag: String,
    val thumbnailUrl: String,
    val title: String,
) {
    fun toModel(): UserWishListItem =
        UserWishListItem(
            name = title,
            tag = tag,
            id = serviceId,
            image = thumbnailUrl,
            category = category,
            minimumMember = minimumMember.toInt(),
            currentMember = currentMember.toInt(),
            basePrice = basePrice,
            discountRatio = discountRatio,
            discountedPrice = discountedPrice,
            deadLine = Date.parse(deadline),
            companyName = companyName,
            companyId = companyId,
            region = region.toModel(),
        )
}
