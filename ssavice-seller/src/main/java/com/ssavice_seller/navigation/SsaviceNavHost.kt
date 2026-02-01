package com.ssavice_seller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssavice.post_service.navigation.addServiceScreen
import com.ssavice.post_service.navigation.navigateToAddService
import com.ssavice.seller_home.navigation.loginScreen
import com.ssavice.seller_home.navigation.navigateToHome
import com.ssavice.seller_main.navigation.mainScreen
import com.ssavice.seller_register.navigation.registerScreen
import kotlinx.serialization.Serializable

@Composable
fun SsaviceNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: @Serializable Any,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        registerScreen(
            onSubmit = {
                navController.navigateToHome {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
        )

        addServiceScreen(
            onDismiss = {
                navController.popBackStack()
            },
            onSubmit = {
                navController.popBackStack()
            },
        )

        loginScreen(
            onLoginComplete = {
                navController.navigateToHome {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
            isUser = false,
        )

        mainScreen(
            onAddClick = {
                navController.navigateToAddService()
            },
        )
    }
}
