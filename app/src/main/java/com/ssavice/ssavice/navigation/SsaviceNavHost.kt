package com.ssavice.ssavice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssavice.edit_profile.navigation.editProfileScreen
import com.ssavice.edit_profile.navigation.navigateToEditProfile
import com.ssavice.model.enums.Category
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
import com.ssavice.user_home.navigation.HomeRoute
import com.ssavice.user_home.navigation.home
import com.ssavice.user_home.navigation.navigateToHome
import com.ssavice.user_main.navigation.mainScreen
import com.ssavice.user_my_page.navigation.myPage
import com.ssavice.user_my_service.navigation.myServiceScreen
import com.ssavice.user_my_service.navigation.navigateToMyService
import kotlinx.serialization.Serializable

@Composable
fun SsaviceNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: @Serializable Any,
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
            }
        )

        loginScreen(
            onLoginComplete = {
                navController.navigateToHome {
                    popUpTo(LoginRoute) { inclusive = true }
                }
            },
            isUser = true,
        )
        searchFormScreen(
            onSearch = { searchForm ->
                navController.navigateToSearchResult(
                    navOptions = {
                        popUpTo(HomeRoute) {
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
            onBack = {
                navController.navigateUp()
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
            onBack = {navController.navigateUp()}
        )

        serviceDetailScreen(
            onBack = {navController.navigateUp()}
        )


        myServiceScreen(
            onServiceClick = {
                navController.navigateToServiceDetail(serviceId = it)
            },
            onReviewClick = { id, name, thumbnailUrl, sellerId ->
                navController.navigateToPostReview(
                    serviceId = id,
                    serviceName = name,
                    serviceThumbnailUrl = thumbnailUrl,
                    sellerId = sellerId,
                )
            },
            onBack = {
                navController.navigateUp()
            }
        )

        editProfileScreen(
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
            }
        )
    }
}
