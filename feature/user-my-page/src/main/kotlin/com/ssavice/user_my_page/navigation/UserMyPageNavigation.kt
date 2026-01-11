package com.ssavice.user_my_page.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_my_page.MyPageRoute
import kotlinx.serialization.Serializable

@Serializable
object UserMyPageRoute

fun NavController.navigateToMyPage(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(UserMyPageRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.myPageScreen(
    onEditProfileButtonClick: () -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
    onScreenResolved: () -> Unit,
) {
    composable<UserMyPageRoute>(
        enterTransition = {
            val isBottomBarNavigation =
                (targetState.destination.route?.contains("UserMyPageRoute") == true)
                        && (initialState.destination.route?.contains("MainRoute") == true)
            if(isBottomBarNavigation) {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(),
                )
            }
            else {
                null
            }
        },
        exitTransition = {
            val isBottomBarNavigation =
                (initialState.destination.route?.contains("UserMyPageRoute") == true)
                        && (targetState.destination.route?.contains("MainRoute") == true)
            if(isBottomBarNavigation){
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(),
                )
            }
            else {
                null
            }
        },
    ) {backStackEntry ->
        val lifecycleState by backStackEntry.lifecycle.currentStateFlow.collectAsStateWithLifecycle()

        LaunchedEffect(lifecycleState) {
            if(lifecycleState == Lifecycle.State.STARTED){
                onScreenResolved()
            }
        }
        MyPageRoute(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onEditProfileButtonClick = onEditProfileButtonClick,
            onParticipatedServiceButtonClick = onParticipatedServiceButtonClick,
            onLikedServiceButtonClick = onLikedServiceButtonClick,
            onHelpButtonClick = onHelpButtonClick,
            onLogoutButtonClick = onLogoutButtonClick,
            onWithdrawButtonClick = onWithdrawButtonClick,
        )
    }
}
