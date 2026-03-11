package com.ssavice.model.service

import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SearchRange
import com.ssavice.model.enums.SortingOrder
import kotlinx.serialization.Serializable

@Serializable
data class SearchQuery(
    val category: Category,
    val query: String,
    val region1: String = "",
    val region2: String = "",
    val searchRange: SearchRange,
    val minPrice: Int,
    val maxPrice: Int,
    val sortBy: SortingOrder,
    val latitude: Double,
    val longitude: Double,
    val onSaleOnly: Boolean,
)
