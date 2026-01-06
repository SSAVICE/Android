package com.ssavice.search_result.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.model.service.SearchQuery
import com.ssavice.search_result.SearchResultScreen
import kotlinx.serialization.Serializable

@Serializable
data class SearchResultRoute(
    val query: String = "",
    val selectedCategory: Int = 0,
    val searchRange: Int = 0,
    val startPrice: Int = 0,
    val endPrice: Int = 10_000_000,
    val sortBy: Int = 0,
)

fun NavController.navigateToSearchResult(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    searchQuery: SearchQuery,
) {
    navigate(
        SearchResultRoute(
            query = searchQuery.query,
            selectedCategory = searchQuery.category.index,
            searchRange = searchQuery.searchRange,
            startPrice = searchQuery.minPrice,
            endPrice = searchQuery.maxPrice,
            sortBy = searchQuery.sortBy.value,
        ),
    ) {
        navOptions()
    }
}

fun NavGraphBuilder.searchResultScreen(
    onSearchBarClicked: (SearchQuery) -> Unit = {},
    onServiceClicked: (Long) -> Unit = {},
) {
    composable<SearchResultRoute> {
        SearchResultScreen(
            onSearchBarClicked = onSearchBarClicked,
            onServiceClicked = onServiceClicked,
        )
    }
}

object SearchResultRouteContract {
    const val QUERY = "query"
    const val SELECTED_CATEGORY = "selectedCategory"
    const val SEARCH_RANGE = "searchRange"
    const val START_PRICE = "startPrice"
    const val END_PRICE = "endPrice"
    const val SORT_BY = "sortBy"
}
