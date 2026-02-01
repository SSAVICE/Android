package com.ssavice.post_service.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.designsystem.component.SsavicePopUpTopBar
import com.ssavice.post_service.AddServiceRoute
import kotlinx.serialization.Serializable

@Serializable
object AddServiceRoute

fun NavController.navigateToAddService(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = AddServiceRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.addServiceScreen(
    onDismiss: () -> Unit = {},
    onSubmit: (Long) -> Unit = {},
) {
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
        Scaffold(
            topBar = {
                SsavicePopUpTopBar(
                    "서비스 추가",
                    onBackClicked = onDismiss,
                )
            },
        ) { innerPadding ->
            AddServiceRoute(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
                onSubmit = onSubmit,
                onDismiss = onDismiss,
            )
        }
    }
}
