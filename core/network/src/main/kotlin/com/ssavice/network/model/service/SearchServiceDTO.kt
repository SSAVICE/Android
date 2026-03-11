package com.ssavice.network.model.service

import com.ssavice.model.Date
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.mapState
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.SearchResultItem
import com.ssavice.network.model.RegionDTO
import kotlinx.serialization.Serializable

@Serializable
data class SearchServiceDTO(
    val category: String,
    val query: String,
    val region1: String,
    val region2: String,
    val latitude: Double,
    val longitude: Double,
    val range: Int,
    val minPrice: Long,
    val maxPrice: Long,
    val sortBy: Int,
    val lastId: Long?,
    val size: Int,
    val onSale: Boolean = true,
) {
    fun toMap(): Map<String, String> =
        if (lastId != null) {
            mapOf(
                "category" to category,
                "query" to query,
                "gugun" to region1,
                "region" to region2,
                "range" to range.toString(),
                "minPrice" to minPrice.toString(),
                "maxPrice" to maxPrice.toString(),
                "sortBy" to sortBy.toString(),
                "lastId" to lastId.toString(),
                "size" to size.toString(),
                "userLatitude" to latitude.toString(),
                "userLongitude" to longitude.toString(),
                "onSale" to onSale.toString(),
            )
        } else {
            mapOf(
                "category" to category,
                "query" to query,
                "gugun" to region1,
                "region" to region2,
                "range" to range.toString(),
                "minPrice" to minPrice.toString(),
                "maxPrice" to maxPrice.toString(),
                "sortBy" to sortBy.toString(),
                "size" to size.toString(),
                "userLatitude" to latitude.toString(),
                "userLongitude" to longitude.toString(),
                "onSale" to onSale.toString(),
            )
        }

    companion object {
        fun fromModel(
            query: SearchQuery,
            nextId: Long?,
            searchCount: Int,
        ): SearchServiceDTO =
            SearchServiceDTO(
                category = query.category.name,
                query = query.query,
                region1 = query.region1,
                region2 = query.region2,
                range = query.searchRange.ordinal,
                minPrice = query.minPrice.toLong(),
                maxPrice = query.maxPrice.toLong(),
                sortBy = query.sortBy.index,
                lastId = nextId,
                size = searchCount,
                latitude = query.latitude,
                longitude = query.longitude,
                onSale = query.onSaleOnly
            )
    }
}

@Serializable
data class SearchServiceResponseDTO(
    val content: List<SearchServiceItemDTO>,
    val hasNext: Boolean,
    val nextCursor: Long? = null,
) {
    fun toModel(): SearchResult =
        SearchResult(
            items = content.map { it.toModel() },
            hasNext = hasNext,
            nextCursor = nextCursor ?: -1,
        )
}

@Serializable
data class SearchServiceItemDTO(
    val serviceId: Long,
    val serviceImageUrl: String?,
    val category: String,
    val title: String,
    val tag: String,
    val status: String,
    val companyId: Long,
    val companyName: String,
    val region: RegionDTO,
    val currentMember: Long,
    val minimumMember: Long,
    val maximumMember: Long,
    val basePrice: Long,
    val discountRatio: Long,
    val discountedPrice: Long,
    val deadline: String,
    val isBooked: Boolean,
) {
    fun toModel(): SearchResultItem =
        SearchResultItem(
            name = title,
            tag = tag,
            id = serviceId,
            image = serviceImageUrl ?: "",
            category = category,
            minimumMember = minimumMember.toInt(),
            currentMember = currentMember.toInt(),
            basePrice = basePrice,
            discountRatio = discountRatio.toInt(),
            discountedPrice = discountedPrice,
            deadLine = Date.parse(deadline),
            companyName = companyName,
            companyId = companyId,
            region = region.toModel(),
            booked = isBooked,
            state = ServiceState.mapState(status)
        )
}
