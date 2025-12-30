package com.ssavice_seller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ssavice.seller_main.navigation.mainScreen
import com.ssavice.seller_main.navigation.navigateToMain
import com.ssavice.seller_register.navigation.RegisterRoute
import com.ssavice.seller_register.navigation.registerScreen

@Composable
fun SsaviceNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RegisterRoute,
        modifier = modifier,
    ) {
        mainScreen()
        registerScreen(
            onSubmit = {
                navController.navigateToMain()
            },
        )
    }
}
