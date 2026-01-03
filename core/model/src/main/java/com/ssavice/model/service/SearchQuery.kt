package com.ssavice.model.service

import kotlinx.serialization.Serializable

@Serializable
data class SearchQuery(
    val category: String,
    val query: String,
    val region1: String,
    val region2: String,
    val searchRange: Int,
    val minPrice: Int,
    val maxPrice: Int,
    val sortBy: SortingOrder,
)

@Serializable
enum class SortingOrder(
    val value: Int,
) {
    POPULARITY(0),
    PRICE_ASC(1),
    PRICE_DESC(2),
}
