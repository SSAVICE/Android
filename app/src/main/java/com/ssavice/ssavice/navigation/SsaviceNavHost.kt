package com.ssavice.ssavice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssavice.edit_profile.navigation.editProfileScreen
import com.ssavice.edit_profile.navigation.navigateToEditProfile
import com.ssavice.login.navigation.LoginRoute
import com.ssavice.login.navigation.loginScreen
import com.ssavice.model.enums.Category
import com.ssavice.model.service.SearchQuery
import com.ssavice.search.SearchForm
import com.ssavice.search.navigation.navigateToSearchForm
import com.ssavice.search.navigation.searchFormScreen
import com.ssavice.search_result.navigation.navigateToSearchResult
import com.ssavice.search_result.navigation.searchResultScreen
import com.ssavice.seller_detail.navigation.navigateToSellerDetail
import com.ssavice.seller_detail.navigation.sellerDetailScreen
import com.ssavice.seller_reviews.navigation.navigateToSellerReviews
import com.ssavice.seller_reviews.navigation.sellerReviewsScreen
import com.ssavice.service_detail.navigation.navigateToPostReview
import com.ssavice.service_detail.navigation.navigateToServiceDetail
import com.ssavice.service_detail.navigation.postReviewScreen
import com.ssavice.service_detail.navigation.serviceDetailScreen
import com.ssavice.user_liked.navigation.likedScreen
import com.ssavice.user_liked.navigation.navigateToUserLiked
import com.ssavice.user_main.navigation.MainRoute
import com.ssavice.user_main.navigation.mainScreen
import com.ssavice.user_main.navigation.navigateToMain
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
                navController.navigateToServiceDetail(serviceId = it, isSeller = false)
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
            onLikedServiceButtonClick = {
                navController.navigateToUserLiked()
            },
            onServiceSummaryClick = {
                navController.navigateToMyService(searchFilter = it)
            }
        )

        loginScreen(
            onLoginComplete = {
                navController.navigateToMain {
                    popUpTo(LoginRoute) { inclusive = true }
                }
            },
            isUser = true,
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
                            latitude = 0.0,
                            longitude = 0.0,
                        ),
                )
            },
            onBack = {
                navController.navigateUp()
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
                navController.navigateToServiceDetail(serviceId = serviceId, isSeller = false)
            },
            onBack = { navController.navigateUp() },
        )

        serviceDetailScreen(
            onBack = { navController.navigateUp() },
            onSellerClick = {
                navController.navigateToSellerDetail(sellerId = it)
            },
            onMoreReviewClick = {
                navController.navigateToSellerReviews(sellerId = it)
            },
        )

        myServiceScreen(
            onServiceClick = {
                navController.navigateToServiceDetail(serviceId = it, isSeller = false)
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
            },
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
            },
        )

        likedScreen(
            onServiceClick = {
                navController.navigateToServiceDetail(serviceId = it, isSeller = false)
            },
            onBack = {
                navController.navigateUp()
            },
        )

        sellerDetailScreen(
            onBackClick = {
                navController.navigateUp()
            },
            onMoreReview = {
                navController.navigateToSellerReviews(sellerId = it)
            },
            onServiceClick = {
                navController.navigateToServiceDetail(serviceId = it, isSeller = false)
            },
        )

        sellerReviewsScreen(
            onBackClick = {
                navController.navigateUp()
            },
        )
    }
}
