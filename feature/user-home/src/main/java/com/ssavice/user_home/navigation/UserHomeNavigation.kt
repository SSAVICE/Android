package com.ssavice.user_home.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_home.UserHomeScreen
import com.ssavice.user_home.UserHomeViewModel
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(HomeRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.home(
    viewModel: UserHomeViewModel,
    onSearch: () -> Unit = {},
    onServiceClick: (Long) -> Unit = {},
) {
    composable<HomeRoute>(
        enterTransition = {
            val isBottomBarNavigation =
                (initialState.destination.route?.contains("UserMyPageRoute") == true) &&
                        (targetState.destination.route?.contains("HomeRoute") == true)
            if (isBottomBarNavigation) {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(),
                )
            } else {
                null
            }
        },
        exitTransition = {
            val isBottomBarNavigation =
                (targetState.destination.route?.contains("UserMyPageRoute") == true) &&
                        (initialState.destination.route?.contains("HomeRoute") == true)
            if (isBottomBarNavigation) {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(),
                )
            } else {
                null
            }
        },
    ) {
        UserHomeScreen(
            viewModel = viewModel,
            onSearchBarClicked = onSearch,
            onServiceClick = onServiceClick,
        )
    }
}
