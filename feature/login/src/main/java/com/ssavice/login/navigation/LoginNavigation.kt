package com.ssavice.seller_home.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
    isUser: Boolean,
) {
    composable<LoginRoute>
    { backStackEntry ->
        Scaffold { innerPadding ->
            LoginRoute(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                onLoginComplete = onLoginComplete,
                viewModel = hiltViewModel(),
                isUser = isUser,
            )
        }
    }
}
