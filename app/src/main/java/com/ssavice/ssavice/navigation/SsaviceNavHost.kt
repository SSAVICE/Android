package com.ssavice.ssavice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ssavice.model.Category
import com.ssavice.model.service.SearchQuery
import com.ssavice.search.SearchForm
import com.ssavice.search.navigation.navigateToSearchForm
import com.ssavice.search.navigation.searchFormScreen
import com.ssavice.search_result.navigation.navigateToSearchResult
import com.ssavice.search_result.navigation.searchResultScreen
import com.ssavice.service_detail.navigation.navigateToServiceDetail
import com.ssavice.service_detail.navigation.serviceDetailScreen
import com.ssavice.user_main.navigation.MainRoute
import com.ssavice.user_main.navigation.mainScreen

@Composable
fun SsaviceNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MainRoute,
        modifier = modifier,
    ) {
        mainScreen(
            onSearch = {
                navController.navigateToSearchForm()
            },
            onServiceClick = {
                navController.navigateToServiceDetail(serviceId = it)
            }
        )
        searchFormScreen(
            onSearch = { searchForm ->
                navController.navigateToSearchResult(
                    navOptions = {
                        popUpTo(MainRoute) {
                            inclusive = false
                        }
                    },
                    searchQuery =
                        SearchQuery(
                            category =
                                Category.entries.getOrElse(
                                    searchForm.selectedCategory,
                                    { Category.entries[0] },
                                ),
                            query = searchForm.query,
                            minPrice = searchForm.priceRange.first,
                            maxPrice = searchForm.priceRange.last,
                            searchRange = searchForm.searchRange,
                            sortBy = searchForm.sortBy,
                        ),
                )
            },
        )

        searchResultScreen(
            onSearchBarClicked = { query ->
                navController.navigateToSearchForm(
                    searchForm =
                        SearchForm(
                            query = query.query,
                            categories = Category.entries.map { it.value },
                            selectedCategory = query.category.index,
                            searchRange = query.searchRange,
                            priceRange = query.minPrice..query.maxPrice,
                            sortBy = query.sortBy,
                        ),
                )
            },
            onServiceClicked = { serviceId ->
                navController.navigateToServiceDetail(serviceId = serviceId)
            },
        )

        serviceDetailScreen(

        )
    }
}
