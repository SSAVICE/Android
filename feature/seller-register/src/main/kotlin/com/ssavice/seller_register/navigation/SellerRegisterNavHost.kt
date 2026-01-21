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
import com.ssavice.model.TimeStamp
import com.ssavice.seller_register.AddressForm
import com.ssavice.seller_register.ValidationState

@Composable
fun SellerRegisterNavHost(
    modifier: Modifier = Modifier,
    page: Int,
    sellerNameState: TextFieldState,
    businessOwnerState: TextFieldState,
    businessRegistrationNumberState: TextFieldState,
    companyOpenDate: TimeStamp,
    onCompanyOpenDateChanged: (TimeStamp) -> Unit,
    onValidateButtonClicked: () -> Unit,
    onAddressSelected: (AddressForm) -> Unit,
    telState: TextFieldState,
    companyValidationState: ValidationState,
    sellerNameError: Boolean,
    businessOwnerError: Boolean,
    businessRegistrationNumberError: Boolean,
    telError: Boolean,
    addressState: AddressForm,
    detailAddressState: TextFieldState,
    descriptionState: TextFieldState,
    addressError: Boolean,
    accountDepositorState: TextFieldState,
    accountNumberState: TextFieldState,
    accountDepositorError: Boolean,
    accountNumberError: Boolean,
    companyOpenDateError: Boolean,
    tokenRemainingTime: Long,
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
            businessOwnerState = businessOwnerState,
            businessRegistrationNumberState = businessRegistrationNumberState,
            companyOpenDate = companyOpenDate,
            companyValidationState = companyValidationState,
            businessOwnerError = businessOwnerError,
            businessRegistrationNumberError = businessRegistrationNumberError,
            onCompanyOpenDateChanged = onCompanyOpenDateChanged,
            onValidateButtonClicked = onValidateButtonClicked,
            tokenRemainingTime = tokenRemainingTime,
        )
        secondPage(
            modifier = Modifier.padding(horizontal = 5.dp),
            detailAddressState = detailAddressState,
            addressState = addressState,
            descriptionState = descriptionState,
            sellerNameState = sellerNameState,
            telState = telState,
            sellerNameError = sellerNameError,
            addressError = addressError,
            telError = telError,
            onAddressSelected = onAddressSelected,
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
