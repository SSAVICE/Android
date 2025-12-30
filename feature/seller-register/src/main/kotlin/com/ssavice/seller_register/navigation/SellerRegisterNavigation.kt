package com.ssavice.seller_register.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.seller_register.RegisterScreen
import kotlinx.serialization.Serializable

@Serializable
object RegisterRoute

fun NavController.navigateToRegister(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = RegisterRoute) {
        popUpTo(RegisterRoute) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.registerScreen(onSubmit: () -> Unit) {
    composable<RegisterRoute>
    {
        RegisterScreen(
            onSubmit = onSubmit,
        )
    }
}
