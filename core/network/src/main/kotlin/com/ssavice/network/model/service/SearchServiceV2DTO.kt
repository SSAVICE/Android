package com.ssavice.network.model.service

import com.ssavice.model.Date
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.SearchResultItem
import com.ssavice.network.model.RegionDTO
import kotlinx.serialization.Serializable

@Serializable
data class SearchServiceV2DTO(
    val category: String,
    val query: String,
    val region1: String,
    val region2: String,
    val latitude: Double,
    val longitude: Double,
    val range: String,
    val minPrice: Long,
    val maxPrice: Long,
    val sortBy: String,
    val lastId: Long?,
    val searchAfter: List<String>?,
    val size: Int,
    val onSale: Boolean = true,
) {
    fun toMap(): Map<String, Any?> {
        val params = mutableMapOf<String, Any?>(
            "category" to category,
            "query" to query,
            "gugun" to region1,
            "region" to region2,
            "range" to range,
            "minPrice" to minPrice.toString(),
            "maxPrice" to maxPrice.toString(),
            "sortBy" to sortBy,
            "size" to size.toString(),
            "userLatitude" to latitude.toString(),
            "userLongitude" to longitude.toString(),
            "onSale" to onSale.toString(),
        )
        lastId?.let { params["lastId"] = it }
        searchAfter?.let { params["searchAfter"] = it }
        return params
    }

    companion object {
        fun fromModel(
            query: SearchQuery,
            nextId: Long?,
            searchCount: Int,
            searchAfter: List<String>? = null
        ): SearchServiceV2DTO =
            SearchServiceV2DTO(
                category = query.category.name,
                query = query.query,
                region1 = query.region1,
                region2 = query.region2,
                range = query.searchRange.name,
                minPrice = query.minPrice.toLong(),
                maxPrice = query.maxPrice.toLong(),
                sortBy = query.sortBy.name,
                lastId = nextId,
                size = searchCount,
                latitude = query.latitude,
                longitude = query.longitude,
                onSale = query.onSaleOnly,
                searchAfter = searchAfter
            )
    }
}

@Serializable
data class SearchServiceV2ResponseDTO(
    val content: List<SearchServiceItemDTO>,
    val hasNext: Boolean,
    val searchAfter: List<String>,
    val nextCursor: Long? = null,
) {
    fun toModel(): SearchResult =
        SearchResult(
            items = content.map { it.toModel() },
            hasNext = hasNext,
            nextCursor = nextCursor ?: -1,
            searchAfter = searchAfter
        )
}

@Serializable
data class SearchServiceV2ItemDTO(
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
        )
}
