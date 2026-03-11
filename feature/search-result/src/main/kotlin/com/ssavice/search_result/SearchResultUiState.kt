package com.ssavice.search_result

import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SearchRange
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.service.SearchQuery
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultUiState(
    val searchQuery: SearchQuery =
        SearchQuery(
            query = "",
            region1 = "",
            region2 = "",
            category = Category.entries[0],
            searchRange = SearchRange.entries[0],
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            sortBy = SortingOrder.entries[0],
            latitude = 0.0,
            longitude = 0.0,
            onSaleOnly = true,
        ),
    val searchCount: Int = 10,
)
