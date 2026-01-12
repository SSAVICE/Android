package com.ssavice.user_main.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_main.UserMainScreen
import com.ssavice.user_main.UserMainViewModel
import kotlinx.serialization.Serializable

@Serializable
object MainRoute

fun NavController.navigateToMain(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(MainRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.mainScreen(
    onSearch: () -> Unit = {},
    onServiceClick: (Long) -> Unit = {},
    onScreenResolved: (
        viewModel: UserMainViewModel,
    ) -> Unit,
) {
    composable<MainRoute>(
        enterTransition = {
            val isBottomBarNavigation =
                (initialState.destination.route?.contains("UserMyPageRoute") == true) &&
                    (targetState.destination.route?.contains("MainRoute") == true)
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
                    (initialState.destination.route?.contains("MainRoute") == true)
            if (isBottomBarNavigation) {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(),
                )
            } else {
                null
            }
        },
    ) { backStackEntry ->
        val lifecycleState by backStackEntry.lifecycle.currentStateFlow.collectAsStateWithLifecycle()
        val viewModel: UserMainViewModel = hiltViewModel()

        LaunchedEffect(lifecycleState) {
            if (lifecycleState == Lifecycle.State.STARTED ||
                lifecycleState == Lifecycle.State.RESUMED ||
                lifecycleState == Lifecycle.State.CREATED
            ) {
                onScreenResolved(viewModel)
            }
        }
        UserMainScreen(
            viewModel = viewModel,
            onSearchBarClicked = onSearch,
            onServiceClick = onServiceClick,
        )
    }
}
