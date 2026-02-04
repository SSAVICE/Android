package com.ssavice.seller_detail.navigation

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
import com.ssavice.seller_detail.SellerDetailRoute
import kotlinx.serialization.Serializable

@Serializable
data class SellerDetailRoute(
    val sellerId: Long,
)

fun NavController.navigateToSellerDetail(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    sellerId: Long,
) {
    navigate(SellerDetailRoute(sellerId)) {
        navOptions()
    }
}

fun NavGraphBuilder.sellerDetailScreen(
    onBackClick: () -> Unit = {},
    onMoreService: (Long) -> Unit = {},
    onMoreReview: (Long) -> Unit = {},
) {
    composable<SellerDetailRoute>(
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
                    title = "판매자 상세 정보",
                    onBackClicked = onBackClick,
                )
            },
        ) { innerPadding ->
            SellerDetailRoute(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                onMoreReviewClick = onMoreReview,
                onMoreServiceClick = onMoreService
            )
        }
    }
}

object SellerDetailRouteContract {
    const val ID = "sellerId"
}
