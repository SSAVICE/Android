package com.ssavice.ui.searchresult

import com.ssavice.model.service.SortingOrder

data class SearchResultUiState(
    val items: List<SearchResultItemUiState>,
    val status: SearchStatus,
    val searchQuery: SearchQuery,
    val hasNext: Boolean,
    val nextId: Long,
)

data class SearchResultItemUiState(
    val name: String,
    val tag: String,
    val id: Long,
    val index: Int,
    val imageUrl: String,
    val companyName: String,
    val address: String,
    val distance: String,
    val deadLine: String,
    val discountedPrice: Int,
    val basePrice: Int,
    val discountRatio: Int,
    val memberStatus: String
)

sealed interface SearchStatus {
    object Loading : SearchStatus
    object Shown : SearchStatus
    object Error : SearchStatus
}

data class SearchQuery(
    val query: String,
    val region1: String,
    val region2: String,
    val category: String,
    val searchRange: Int,
    val minPrice: Int,
    val maxPrice: Int,
    val sortBy: SortingOrder,
    val searchCount: Int = 10,
)
