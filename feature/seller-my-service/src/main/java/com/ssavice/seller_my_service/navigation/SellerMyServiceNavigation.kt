package com.ssavice.seller_my_service.navigation

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
import com.ssavice.seller_my_service.MyServiceRoute
import kotlinx.serialization.Serializable

@Serializable
data object SellerMyServiceRoute

fun NavController.navigateToMyService(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(SellerMyServiceRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.myServiceScreen(
    onServiceClick: (Long) -> Unit,
    onBack: () -> Unit = {},
) {
    composable<SellerMyServiceRoute>(
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
                    title = "참여 서비스",
                    onBackClicked = onBack,
                )
            },
        ) { innerPadding ->
            MyServiceRoute(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                onServiceClick = onServiceClick,
            )
        }
    }
}
