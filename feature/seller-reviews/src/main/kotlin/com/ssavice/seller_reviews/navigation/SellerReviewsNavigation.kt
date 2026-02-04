package com.ssavice.seller_reviews.navigation

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
import com.ssavice.seller_reviews.SellerReviewRoute
import kotlinx.serialization.Serializable

@Serializable
data class SellerReviewsRoute(
    val sellerId: Long,
)

fun NavController.navigateToSellerReviews(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    sellerId: Long,
) {
    navigate(SellerReviewsRoute(sellerId)) {
        navOptions()
    }
}

fun NavGraphBuilder.sellerReviewsScreen(onBackClick: () -> Unit = {}) {
    composable<SellerReviewsRoute>(
        popEnterTransition = null,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(),
            )
        },
    ) {
        Scaffold(
            topBar = {
                SsavicePopUpTopBar(
                    title = "판매자 리뷰",
                    onBackClicked = onBackClick,
                )
            },
        ) { innerPadding ->
            SellerReviewRoute(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
            )
        }
    }
}

object SellerReviewsRouteContract {
    const val ID = "sellerId"
}
