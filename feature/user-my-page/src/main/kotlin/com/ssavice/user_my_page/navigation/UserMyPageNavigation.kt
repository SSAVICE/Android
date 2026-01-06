package com.ssavice.user_my_page.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_my_page.MyPageRoute
import kotlinx.serialization.Serializable

@Serializable
data object UserMyPageRoute

fun NavController.navigateToMyPage(
    navOptions: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(UserMyPageRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.myPageScreen(
    onEditProfileButtonClick:()->Unit = {},
    onParticipatedServiceButtonClick:()->Unit = {},
    onLikedServiceButtonClick:()->Unit = {},
    onHelpButtonClick:()->Unit = {},
    onLogoutButtonClick:()->Unit = {},
    onWithdrawButtonClick:()->Unit = {},
    onScreenResolved: () -> Unit,
) {
    composable<UserMyPageRoute>(
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
