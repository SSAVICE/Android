package com.ssavice.user_chatting.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_chatting.UserChattingRoute
import kotlinx.serialization.Serializable

@Serializable
object UserChattingRoute

fun NavController.navigateToChat(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(UserChattingRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.chat(
    onRoomClick:(id: Long)->Unit = {}
) {
    composable<UserChattingRoute>(
        enterTransition = {
            val fromLeft =
                initialState.destination.route?.contains("HomeRoute") == true
            val direction =
                if (fromLeft) {
                    AnimatedContentTransitionScope.SlideDirection.Left
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Right
                }

            slideIntoContainer(
                towards = direction,
                animationSpec = tween(),
            )
        },
        exitTransition = {
            val toLeft =
                (targetState.destination.route?.contains("HomeRoute") == true)
            val direction =
                if (toLeft) {
                    AnimatedContentTransitionScope.SlideDirection.Right
                } else {
                    AnimatedContentTransitionScope.SlideDirection.Left
                }

            slideOutOfContainer(
                towards = direction,
                animationSpec = tween(),
            )
        },
    ) {
        UserChattingRoute(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
            onRoomClick = onRoomClick
        )
    }
}
