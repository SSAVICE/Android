package com.ssavice.user_liked.navigation

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
import com.ssavice.user_liked.UserLikedRoute
import kotlinx.serialization.Serializable

@Serializable
data object UserLikedRoute

fun NavController.navigateToUserLiked(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(UserLikedRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.likedScreen(
    onServiceClick: (Long) -> Unit,
    onBack: () -> Unit = {},
) {
    composable<UserLikedRoute>(
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
                    title = "관심 서비스",
                    onBackClicked = onBack,
                )
            },
        ) { innerPadding ->
            UserLikedRoute (
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                onServiceClick = onServiceClick,
            )
        }
    }
}
