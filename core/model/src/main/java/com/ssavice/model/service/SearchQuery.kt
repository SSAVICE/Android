package com.ssavice.model.service

import com.ssavice.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class SearchQuery(
    val category: Category,
    val query: String,
    val region1: String = "",
    val region2: String = "",
    val searchRange: Int,
    val minPrice: Int,
    val maxPrice: Int,
    val sortBy: SortingOrder,
)
