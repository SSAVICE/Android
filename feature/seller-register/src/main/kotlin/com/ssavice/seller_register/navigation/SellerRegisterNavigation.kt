package com.ssavice.seller_register.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.seller_register.RegisterScreen
import com.ssavice.ui.navigation.SsaviceTitle
import kotlinx.serialization.Serializable

@Serializable
object RegisterRoute

fun NavController.navigateToRegister(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = RegisterRoute) {
        popUpTo(graph.startDestinationId) { inclusive = true }
    }
}

fun NavGraphBuilder.registerScreen(onSubmit: () -> Unit) {
    composable<RegisterRoute>
    {
        Scaffold(
            topBar = {
                SsaviceTitle(
                    "회원가입",
                )
            },
        ) { innerPadding ->
            RegisterScreen(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                onSubmit = onSubmit,
            )
        }
    }
}
