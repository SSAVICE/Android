package com.ssavice.login.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.login.LoginRoute
import kotlinx.serialization.Serializable

@Serializable
data class LoginRoute(val isUser: Boolean)

fun NavController.navigateToLogin(navOptions: NavOptionsBuilder.() -> Unit = {}, isUser: Boolean) {
    navigate(route = LoginRoute(isUser)) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.loginScreen(
    onLoginComplete: () -> Unit = {},
    isUser: Boolean,
) {
    composable<LoginRoute>
    { backStackEntry ->
        Scaffold(
            containerColor = Color.Transparent
        ) { innerPadding ->
            LoginRoute(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                onLoginComplete = onLoginComplete,
                viewModel = hiltViewModel(),
            )
        }
    }
}

object LoginNavigationContract {
    const val IS_USER = "isUser"
}
