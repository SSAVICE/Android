package com.ssavice.seller_register.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun SellerRegisterNavHost(
    modifier: Modifier = Modifier,
    page: Int,
    sellerNameState: TextFieldState,
    businessOwnerState: TextFieldState,
    businessRegistrationNumberState: TextFieldState,
    telState: TextFieldState,
    sellerNameError: Boolean,
    businessOwnerError: Boolean,
    businessRegistrationNumberError: Boolean,
    telError: Boolean,
    addressState: TextFieldState,
    descriptionState: TextFieldState,
    addressError: Boolean,
    accountDepositorState: TextFieldState,
    accountNumberState: TextFieldState,
    accountDepositorError: Boolean,
    accountNumberError: Boolean,
) {
    val navController = rememberNavController()
    val currentRoute = navController.currentDestination?.route

    LaunchedEffect(page) {
        if (routeNumber[page] != currentRoute) {
            val left = routeNumber[page + 1] == currentRoute
            when (page) {
                1 -> navController.navigateToFirstPage(left)
                2 -> navController.navigateToSecondPage(left)
                3 -> navController.navigateToThirdPage(left)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = FirstPageRoute,
        modifier = modifier,
        enterTransition = {
            val initialRouteNumber = routeNumber.values.indexOf(initialState.destination.route)
            val targetRouteNumber = routeNumber.values.indexOf(targetState.destination.route)
            val toLeft = (targetRouteNumber < initialRouteNumber)
            if (toLeft) {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween())
            } else {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween())
            }
        },
        exitTransition = {
            val initialRouteNumber = routeNumber.values.indexOf(initialState.destination.route)
            val targetRouteNumber = routeNumber.values.indexOf(targetState.destination.route)
            val toLeft = (targetRouteNumber < initialRouteNumber)
            if (toLeft) {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween())
            } else {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween())
            }
        },
    ) {
        firstPage(
            modifier = Modifier.padding(horizontal = 5.dp),
            sellerNameState = sellerNameState,
            businessOwnerState = businessOwnerState,
            businessRegistrationNumberState = businessRegistrationNumberState,
            telState = telState,
            sellerNameError = sellerNameError,
            businessOwnerError = businessOwnerError,
            businessRegistrationNumberError = businessRegistrationNumberError,
            telError = telError,
        )
        secondPage(
            modifier = Modifier.padding(horizontal = 5.dp),
            addressState = addressState,
            descriptionState = descriptionState,
            addressError = addressError,
        )
        thirdPage(
            modifier = Modifier.padding(horizontal = 5.dp),
            accountDepositorState = accountDepositorState,
            accountNumberState = accountNumberState,
            accountDepositorError = accountDepositorError,
            accountNumberError = accountNumberError,
        )
    }
}
