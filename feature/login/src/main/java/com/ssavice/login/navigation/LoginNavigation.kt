package com.ssavice.seller_main.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.login.LoginRoute
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

fun NavController.navigateToLogin(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = LoginRoute) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.loginScreen(
    onLoginComplete: () -> Unit = {},
    onScreenResolved: () -> Unit,
    isUser: Boolean,
) {
    composable<LoginRoute>
    { backStackEntry ->
        val lifecycleState by backStackEntry.lifecycle.currentStateFlow.collectAsStateWithLifecycle()

        LaunchedEffect(lifecycleState) {
            if (lifecycleState == Lifecycle.State.STARTED) {
                onScreenResolved()
            }
        }

        LoginRoute(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            onLoginComplete = onLoginComplete,
            viewModel = hiltViewModel(),
            isUser = isUser,
        )
    }
}
