package com.ssavice.user_main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ssavice.model.service.SortingOrder
import com.ssavice.ui.searchresult.SearchQuery
import com.ssavice.ui.searchresult.SearchResultScreen

@Composable
fun UserMainScreen(
    modifier: Modifier = Modifier,
) {
    SearchResultScreen(
        modifier = modifier,
        query = SearchQuery(
            query = "",
            region1 = "",
            region2 = "",
            category = "",
            searchRange = 1,
            minPrice = 0,
            maxPrice = 10000,
            sortBy = SortingOrder.POPULARITY,
            searchCount = 10
        )
    )
}
