package com.ssavice.search_result.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.designsystem.component.SsavicePopUpTopBar
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
    val onSaleOnly: Boolean = false,
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
            sortBy = searchQuery.sortBy.ordinal,
            onSaleOnly = searchQuery.onSaleOnly,
        ),
    ) {
        launchSingleTop = true
        navOptions()
    }
}

fun NavGraphBuilder.searchResultScreen(
    onSearchBarClicked: (SearchQuery) -> Unit = {},
    onServiceClicked: (Long) -> Unit = {},
    onBack: () -> Unit = {},
) {
    composable<SearchResultRoute>(
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(),
            )
        },
    ) {
        Scaffold(topBar = {
            SsavicePopUpTopBar(
                title = "검색 결과",
                onBackClicked = onBack,
            )
        }) { contentPadding ->
            SearchResultScreen(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(contentPadding),
                onSearchBarClicked = onSearchBarClicked,
                onServiceClicked = onServiceClicked,
            )
        }
    }
}

object SearchResultRouteContract {
    const val QUERY = "query"
    const val SELECTED_CATEGORY = "selectedCategory"
    const val SEARCH_RANGE = "searchRange"
    const val START_PRICE = "startPrice"
    const val END_PRICE = "endPrice"
    const val SORT_BY = "sortBy"
    const val ON_SALE_ONLY = "onSaleOnly"
}
