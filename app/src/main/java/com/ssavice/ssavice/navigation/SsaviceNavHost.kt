package com.ssavice.ssavice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssavice.model.Category
import com.ssavice.model.service.SearchQuery
import com.ssavice.search.SearchForm
import com.ssavice.search.navigation.navigateToSearchForm
import com.ssavice.search.navigation.searchFormScreen
import com.ssavice.search_result.navigation.navigateToSearchResult
import com.ssavice.search_result.navigation.searchResultScreen
import com.ssavice.service_detail.navigation.navigateToServiceDetail
import com.ssavice.service_detail.navigation.serviceDetailScreen
import com.ssavice.user_main.UserMainTopBar
import com.ssavice.user_main.navigation.MainRoute
import com.ssavice.user_main.navigation.mainScreen
import kotlinx.serialization.Serializable

/**
 * 앱의 메인 NavHost.
 *
 * @param onScaffoldConfigResolved 현재 라우트에 맞는 Scaffold 구성을 상위로 전달하는 콜백.
 */
@Composable
fun SsaviceNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: @Serializable Any,
    onScaffoldConfigResolved: (ScaffoldConfig) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        mainScreen(
            onSearch = {
                navController.navigateToSearchForm()
            },
            onServiceClick = {
                navController.navigateToServiceDetail(serviceId = it)
            },
            onScreenResolved = { viewModel ->
                onScaffoldConfigResolved(
                    ScaffoldConfig.CustomTopWithDefaultBottom(
                        topBar = {
                            UserMainTopBar(viewModel)
                        },
                    )
                )
            }
        )
        searchFormScreen(
            onSearch = { searchForm ->
                navController.navigateToSearchResult(
                    navOptions = {
                        popUpTo(MainRoute) {
                            inclusive = false
                        }
                        launchSingleTop = true
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
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                    title = "검색",
                    onBackButtonClick = {
                        navController.navigateUp()
                    }
                ))
            }
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
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                    title = "검색 결과",
                    onBackButtonClick = {
                        navController.popBackStack()
                    }
                ))
            }
        )

        serviceDetailScreen(
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                    title = "상세 정보",
                    onBackButtonClick = {
                        navController.popBackStack()
                    }
                ))
            }
        )
    }
}


// Scaffold 구성을 위한 Sealed Class
sealed interface ScaffoldConfig {
    // 기본 BottomBar를 사용하는 경우
    data object Default : ScaffoldConfig

    // BottomBar/TopBar가 없는 경우
    data object None : ScaffoldConfig

    data class TitleAndDefaultBottom(
        val title: String,
        val onBackButtonClick: (() -> Unit)? = null,
    ) : ScaffoldConfig

    data class TitleWithCustomBottom(
        val title: String,
        val onBackButtonClick: (() -> Unit)? = null,
        val bottomBar: (@Composable () -> Unit)? = null
    ) : ScaffoldConfig

    data class CustomTopWithDefaultBottom(
        val topBar: (@Composable () -> Unit)? = null,
    ) : ScaffoldConfig

    // 완전히 커스텀 UI를 사용하는 경우
    data class Custom(
        val topBar: (@Composable () -> Unit)? = null,
        val bottomBar: (@Composable () -> Unit)? = null
    ) : ScaffoldConfig
}
