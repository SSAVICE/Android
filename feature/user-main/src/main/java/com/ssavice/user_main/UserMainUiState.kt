package com.ssavice.user_main

import com.ssavice.model.service.SortingOrder
import com.ssavice.ui.searchresult.SearchQuery

data class UserMainUiState(
    val categories: List<String> = emptyList(),
    val selected: Int = 0,
    val defaultSearchQuery: SearchQuery = SearchQuery(
        query = "",
        region1 = "",
        region2 = "",
        category = "",
        searchRange = 1,
        minPrice = 0,
        maxPrice = Int.MAX_VALUE,
        sortBy = SortingOrder.POPULARITY,
        searchCount = 10
    )
)
