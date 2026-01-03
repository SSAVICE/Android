package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.Date
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.SearchResultItem
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class SearchServiceDTO(
    val category: String,
    val query: String,
    val region1: String,
    val region2: String,
    val range: Int,
    val minPrice: Long,
    val maxPrice: Long,
    val sortBy: Int,
    val lastId: Long?,
    val size: Int
) {
    fun toMap(): Map<String, String> =
        if (lastId != null) {
            mapOf(
                "category" to category,
                "query" to query,
                "region1" to region1,
                "region2" to region2,
                "range" to range.toString(),
                "minPrice" to minPrice.toString(),
                "maxPrice" to maxPrice.toString(),
                "sortBy" to sortBy.toString(),
                "lastId" to lastId.toString(),
                "size" to size.toString()
            )
        } else {
            mapOf(
                "category" to category,
                "query" to query,
                "region1" to region1,
                "region2" to region2,
                "range" to range.toString(),
                "minPrice" to minPrice.toString(),
                "maxPrice" to maxPrice.toString(),
                "sortBy" to sortBy.toString(),
                "size" to size.toString()
            )
        }

    companion object {
        fun fromModel(
            query: SearchQuery,
            nextId: Long?,
            searchCount: Int
        ): SearchServiceDTO {
            return SearchServiceDTO(
                category = query.category,
                query = query.query,
                region1 = query.region1,
                region2 = query.region2,
                range = query.searchRange,
                minPrice = query.minPrice.toLong(),
                maxPrice = query.maxPrice.toLong(),
                sortBy = query.sortBy.value,
                lastId = nextId,
                size = searchCount
            )
        }
    }
}


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class SearchServiceResponseDTO(
    val content: List<SearchServiceItemDTO>,
    val hasNext: Boolean,
    val nextCursor: Long?
) {
    fun toModel(): SearchResult =
        SearchResult(
            items = content.map { it.toModel() },
            hasNext = hasNext,
            nextCursor = nextCursor ?: -1
        )
}


@SuppressLint("UnsafeOptInUsageError")
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
    val latitude: Double,
    val longitude: Double,
    val region1: String?,
    val region2: String?,
    val currentMember: Long,
    val minimumMember: Long,
    val maximumMember: Long,
    val basePrice: Long,
    val discountRatio: Long,
    val discountedPrice: Long,
    val deadline: String,
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
            latitude = latitude,
            longitude = longitude,
            region1 = region1?:"",
            region2 = region2?:"",
            companyName = companyName,
            companyId = companyId
        )
}
