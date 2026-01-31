package com.ssavice.user_my_service.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_my_service.MyServiceRoute
import kotlinx.serialization.Serializable

@Serializable
data object UserMyServiceRoute

fun NavController.navigateToMyService(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(UserMyServiceRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.myServiceScreen(
    onServiceClick: (Long) -> Unit,
    onReviewClick: (id: Long, name: String, thumbnailUrl: String, companyId: Long) -> Unit,
    onScreenResolved: () -> Unit,
) {
    composable<UserMyServiceRoute>(
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
        onScreenResolved()
        MyServiceRoute(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            onServiceClick = onServiceClick,
            onReviewClick = onReviewClick,
        )
    }
}
