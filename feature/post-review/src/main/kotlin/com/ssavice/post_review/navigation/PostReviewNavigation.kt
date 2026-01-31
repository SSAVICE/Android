package com.ssavice.service_detail.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.post_review.PostReviewRoute
import kotlinx.serialization.Serializable

@Serializable
data class PostReviewRoute(
    val serviceId: Long,
    val serviceName: String,
    val serviceThumbnailUrl: String,
    val sellerId: Long
)

fun NavController.navigateToPostReview(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    serviceId: Long,
    serviceName: String,
    serviceThumbnailUrl: String,
    sellerId: Long
) {
    navigate(PostReviewRoute(serviceId,
        serviceName,
        serviceThumbnailUrl,
        sellerId)) {
        navOptions()
    }
}

fun NavGraphBuilder.postReviewScreen(
    onScreenResolved: () -> Unit,
    onBack: () -> Unit = {},
) {
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
    ) { backStackEntry ->
        val lifecycleState by backStackEntry.lifecycle.currentStateFlow.collectAsStateWithLifecycle()

        LaunchedEffect(lifecycleState) {
            if (lifecycleState == Lifecycle.State.STARTED) {
                onScreenResolved()
            }
        }
        PostReviewRoute(
            onBack = onBack,
        )
    }
}

object PostReviewRouteContract {
    const val ID = "serviceId"
    const val NAME = "serviceName"
    const val THUMBNAIL_URL = "serviceThumbnailUrl"
    const val SELLER_ID = "sellerId"
}
