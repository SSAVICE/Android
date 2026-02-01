package com.ssavice.seller_my_page.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.seller_my_page.SellerMyPageRoute
import kotlinx.serialization.Serializable

@Serializable
object MyPageRoute

fun NavController.navigateToMyPage(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(MyPageRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.myPage(
    onMyServiceClick: () -> Unit = {}
) {
    composable<MyPageRoute>(
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
        SellerMyPageRoute(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
            onParticipatedServiceButtonClick = onMyServiceClick
        )
    }
}
