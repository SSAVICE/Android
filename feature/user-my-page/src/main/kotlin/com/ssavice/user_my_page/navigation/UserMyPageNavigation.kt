package com.ssavice.user_my_page.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.model.enums.ServiceState
import com.ssavice.user_my_page.MyPageRoute
import com.ssavice.user_my_page.ProfileState
import kotlinx.serialization.Serializable

@Serializable
object UserMyPageRoute

fun NavController.navigateToMyPage(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(UserMyPageRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.myPage(
    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
    onServiceSummaryClick: (ServiceState) -> Unit = {}
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
        MyPageRoute(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
            onEditProfileButtonClick = onEditProfileButtonClick,
            onParticipatedServiceButtonClick = onParticipatedServiceButtonClick,
            onLikedServiceButtonClick = onLikedServiceButtonClick,
            onHelpButtonClick = onHelpButtonClick,
            onLogoutButtonClick = onLogoutButtonClick,
            onWithdrawButtonClick = onWithdrawButtonClick,
            onServiceSummaryClick = onServiceSummaryClick
        )
    }
}
