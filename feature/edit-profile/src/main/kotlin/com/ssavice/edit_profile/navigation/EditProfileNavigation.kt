package com.ssavice.edit_profile.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.edit_profile.EditProfileRoute
import kotlinx.serialization.Serializable

@Serializable
object EditProfileRoute

fun NavController.navigateToEditProfile(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = EditProfileRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.editProfileScreen(
    onSubmit: () -> Unit,
    onBackClick: () -> Unit,
    onProfileImageClick: () -> Unit,
    onScreenResolved: () -> Unit,
) {
    composable<EditProfileRoute>(
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
        }
    )
    { backStackEntry ->
        val lifecycleStateFlow by
            backStackEntry.lifecycle.currentStateFlow.collectAsStateWithLifecycle()
        LaunchedEffect(
            lifecycleStateFlow
        ) {
            if (lifecycleStateFlow == androidx.lifecycle.Lifecycle.State.STARTED) {
                onScreenResolved()
            }
        }
        EditProfileRoute(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            viewModel = hiltViewModel(),
            onSubmit = onSubmit,
            onBackClick = onBackClick,
            onProfileImageClick = onProfileImageClick
        )
    }
}
