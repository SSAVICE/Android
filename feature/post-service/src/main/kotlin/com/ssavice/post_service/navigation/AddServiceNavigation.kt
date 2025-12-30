package com.ssavice.post_service.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.post_service.AddServiceRoute
import kotlinx.serialization.Serializable

@Serializable
object AddServiceRoute

fun NavController.navigateToAddService(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = AddServiceRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.addServiceScreen() {
    composable<AddServiceRoute>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(),
            )
        },
    )
    {
        AddServiceRoute()
    }
}
