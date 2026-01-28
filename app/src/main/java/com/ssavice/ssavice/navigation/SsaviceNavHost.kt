package com.ssavice.ssavice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssavice.edit_profile.navigation.editProfileScreen
import com.ssavice.edit_profile.navigation.navigateToEditProfile
import com.ssavice.login.LoginRoute
import com.ssavice.model.Category
import com.ssavice.model.service.SearchQuery
import com.ssavice.search.SearchForm
import com.ssavice.search.navigation.navigateToSearchForm
import com.ssavice.search.navigation.searchFormScreen
import com.ssavice.search_result.navigation.navigateToSearchResult
import com.ssavice.search_result.navigation.searchResultScreen
import com.ssavice.seller_main.navigation.LoginRoute
import com.ssavice.seller_main.navigation.loginScreen
import com.ssavice.service_detail.navigation.navigateToPostReview
import com.ssavice.service_detail.navigation.navigateToServiceDetail
import com.ssavice.service_detail.navigation.postReviewScreen
import com.ssavice.service_detail.navigation.serviceDetailScreen
import com.ssavice.service_detail.ui.ServiceDetailBottomBar
import com.ssavice.ui.navigation.ScaffoldConfig
import com.ssavice.user_main.UserMainTopBar
import com.ssavice.user_main.navigation.MainRoute
import com.ssavice.user_main.navigation.mainScreen
import com.ssavice.user_main.navigation.navigateToMain
import com.ssavice.user_my_page.navigation.myPageScreen
import com.ssavice.user_my_service.navigation.myServiceScreen
import com.ssavice.user_my_service.navigation.navigateToMyService
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
    onScaffoldConfigResolved: (ScaffoldConfig) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        loginScreen(
            onLoginComplete = {
                navController.navigateToMain {
                    popUpTo(LoginRoute) { inclusive = true }
                }
            },
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.None,
                )
            },
            isUser = true,
        )

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
                    ),
                )
            },
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
                        },
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
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                        title = "검색 결과",
                        onBackButtonClick = {
                            navController.popBackStack()
                        },
                    ),
                )
            },
        )

        serviceDetailScreen(
            onScreenResolved = { viewModel ->
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                        title = "상세 정보",
                        onBackButtonClick = {
                            navController.popBackStack()
                        },
                        bottomBar = {
                            ServiceDetailBottomBar(
                                viewModel = viewModel,
                            )
                        },
                    ),
                )
            },
        )

        myPageScreen(
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleAndDefaultBottom(
                        title = "마이 페이지",
                    ),
                )
            },
            onParticipatedServiceButtonClick = {
                navController.navigateToMyService()
            },
            onEditProfileButtonClick = {
                navController.navigateToEditProfile(
                    name = it?.name,
                    email = it?.email,
                    phoneNumber = it?.phoneNumber,
                    profileImageUrl = it?.profileUrl,
                )
            },
        )

        myServiceScreen(
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                        title = "참여 서비스",
                        onBackButtonClick = {
                            navController.navigateUp()
                        },
                    ),
                )
            },
            onServiceClick = {
                navController.navigateToServiceDetail(serviceId = it)
            },
            onReviewClick = { id, name, thumbnailUrl ->
                navController.navigateToPostReview(
                    serviceId = id,
                    serviceName = name,
                    serviceThumbnailUrl = thumbnailUrl,
                )
            },
        )

        editProfileScreen(
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                        title = "프로필 수정",
                    ),
                )
            },
            onSubmit = {
                navController.navigateUp()
            },
            onBackClick = {
                navController.navigateUp()
            },
            onProfileImageClick = { },
        )

        postReviewScreen(
            onBack = {
                navController.navigateUp()
            },
            onScreenResolved = {
                onScaffoldConfigResolved(
                    ScaffoldConfig.TitleWithCustomBottom(
                        title = "리뷰 작성",
                        onBackButtonClick = {
                            navController.navigateUp()
                        }
                    )
                )
            }
        )
    }
}
