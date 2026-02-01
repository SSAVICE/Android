package com.ssavice.search_result

import com.ssavice.model.enums.Category
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.enums.SortingOrder
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultUiState(
    val searchQuery: SearchQuery =
        SearchQuery(
            query = "",
            region1 = "",
            region2 = "",
            category = Category.entries[0],
            searchRange = 1,
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            sortBy = SortingOrder.POPULARITY,
        ),
    val searchCount: Int = 10,
)
