package com.ssavice.search.navigation

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
import com.ssavice.search.SearchForm
import com.ssavice.search.SearchFormScreen
import kotlinx.serialization.Serializable

@Serializable
data class SearchFormRoute(
    val query: String = "",
    val selectedCategory: String = "ALL",
    val searchRange: Int = 0,
    val startPrice: Int = 0,
    val endPrice: Int = 10_000_000,
    val sortBy: Int = 0,
)

fun NavController.navigateToSearchForm(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    searchForm: SearchForm,
) {
    navigate(
        SearchFormRoute(
            query = searchForm.query,
            selectedCategory = searchForm.selectedCategory.name,
            searchRange = searchForm.searchRange,
            startPrice = searchForm.priceRange.first,
            endPrice = searchForm.priceRange.last,
            sortBy = searchForm.sortBy.ordinal,
        ),
    ) {
        navOptions()
    }
}

fun NavController.navigateToSearchForm(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(SearchFormRoute()) {
        navOptions()
    }
}

fun NavGraphBuilder.searchFormScreen(
    onSearch: (SearchForm) -> Unit = {},
    onBack: () -> Unit = {},
) {
    composable<SearchFormRoute>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(),
            )
        },
    ) {
        Scaffold(
            topBar =
                {
                    SsavicePopUpTopBar(
                        title = "검색",
                        onBackClicked = onBack,
                    )
                },
        ) { innerPadding ->
            SearchFormScreen(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background),
                onSearch = onSearch,
            )
        }
    }
}

object SearchFormRouteContract {
    const val QUERY = "query"
    const val SELECTED_CATEGORY = "selectedCategory"
    const val SEARCH_RANGE = "searchRange"
    const val START_PRICE = "startPrice"
    const val END_PRICE = "endPrice"
    const val SORT_BY = "sortBy"
    const val REGION1 = "region1"
    const val REGION2 = "region2"
}
