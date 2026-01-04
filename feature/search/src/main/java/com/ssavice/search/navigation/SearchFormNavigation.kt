package com.ssavice.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.search.SearchForm
import com.ssavice.search.SearchFormScreen
import kotlinx.serialization.Serializable

@Serializable
data class SearchFormRoute(
    val query: String = "",
    val selectedCategory: Int = 0,
    val searchRange: Int = 0,
    val startPrice: Int = 0,
    val endPrice: Int = 10_000_000,
    val sortBy: Int = 0,
)

fun NavController.navigateToSearchForm(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    searchForm: SearchForm
) {
    navigate(
        SearchFormRoute(
            query = searchForm.query,
            selectedCategory = searchForm.selectedCategory,
            searchRange = searchForm.searchRange,
            startPrice = searchForm.priceRange.first,
            endPrice = searchForm.priceRange.last,
            sortBy = searchForm.sortBy.value
        )
    ) {
        navOptions()
    }
}

fun NavController.navigateToSearchForm(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(SearchFormRoute()) {
        navOptions()
    }
}

fun NavGraphBuilder.searchFormScreen(onSearch: (SearchForm) -> Unit = {}) {
    composable<SearchFormRoute> {
        SearchFormScreen(onSearch = onSearch)
    }
}

object SearchFormRouteContract {
    const val QUERY = "query"
    const val SELECTED_CATEGORY = "selectedCategory"
    const val SEARCH_RANGE = "searchRange"
    const val START_PRICE = "startPrice"
    const val END_PRICE = "endPrice"
    const val SORT_BY = "sortBy"
}
