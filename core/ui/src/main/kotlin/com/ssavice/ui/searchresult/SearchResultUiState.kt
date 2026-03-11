package com.ssavice.ui.searchresult

import com.ssavice.model.enums.ServiceState
import com.ssavice.model.service.SearchQuery

data class SearchResultUiState(
    val items: List<SearchResultItemUiState>,
    val status: SearchStatus,
    val searchQuery: SearchQuery,
    val hasNext: Boolean,
    val nextId: Long,
    val searchAfter: List<String> = emptyList(),
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
    val memberStatus: String,
    val booked: Boolean,
    val state: ServiceState,
)

sealed interface SearchStatus {
    object Loading : SearchStatus

    object Shown : SearchStatus

    object Error : SearchStatus
}
