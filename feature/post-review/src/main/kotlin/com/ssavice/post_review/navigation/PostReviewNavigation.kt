package com.ssavice.service_detail.navigation

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
import com.ssavice.post_review.PostReviewRoute
import kotlinx.serialization.Serializable

@Serializable
data class PostReviewRoute(
    val serviceId: Long,
    val serviceName: String,
    val serviceThumbnailUrl: String,
    val sellerId: Long,
)

fun NavController.navigateToPostReview(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    serviceId: Long,
    serviceName: String,
    serviceThumbnailUrl: String,
    sellerId: Long,
) {
    navigate(
        PostReviewRoute(
            serviceId,
            serviceName,
            serviceThumbnailUrl,
            sellerId,
        ),
    ) {
        navOptions()
    }
}

fun NavGraphBuilder.postReviewScreen(onBack: () -> Unit = {}) {
    composable<PostReviewRoute>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(),
            )
        },
    ) {
        Scaffold(
            topBar = {
                SsavicePopUpTopBar(
                    title = "리뷰 작성",
                    onBackClicked = onBack,
                )
            },
        ) { innerPadding ->
            PostReviewRoute(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                onBack = onBack,
            )
        }
    }
}

object PostReviewRouteContract {
    const val ID = "serviceId"
    const val NAME = "serviceName"
    const val THUMBNAIL_URL = "serviceThumbnailUrl"
    const val SELLER_ID = "sellerId"
}
