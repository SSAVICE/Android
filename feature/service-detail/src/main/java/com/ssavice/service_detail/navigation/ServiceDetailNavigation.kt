package com.ssavice.service_detail.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.service_detail.ServiceDetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class ServiceDetailRoute(
    val serviceId: Long,
)

fun NavController.navigateToServiceDetail(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    serviceId: Long,
) {
    navigate(ServiceDetailRoute(serviceId)) {
        navOptions()
    }
}

fun NavGraphBuilder.serviceDetailScreen(
    onBackClick: () -> Unit = {},
    onChatClick: (Long) -> Unit = {},
    onParticipateClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onScreenResolved: () -> Unit
) {
    composable<ServiceDetailRoute>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(),
            )
        },
    ) {
        onScreenResolved()
        ServiceDetailScreen(
            onBackClick = onBackClick,
            onChatClick = onChatClick,
            onParticipateClick = onParticipateClick,
            onLikeClick = onLikeClick
        )
    }
}

object ServiceDetailRouteContract {
    const val ID = "serviceId"
}
