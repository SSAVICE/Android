package com.ssavice.seller_register.navigation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.seller_register.page.FirstPage
import com.ssavice.seller_register.page.SecondPage
import com.ssavice.seller_register.page.ThirdPage
import kotlinx.serialization.Serializable

interface Route {
    val route: String
}

@Serializable
object FirstPageRoute

@Serializable
object SecondPageRoute

@Serializable
object ThirdPageRoute

val routeNumber = mapOf(
    1 to FirstPageRoute::class.qualifiedName,
    2 to SecondPageRoute::class.qualifiedName,
    3 to ThirdPageRoute::class.qualifiedName,
)

fun NavController.navigateToFirstPage(
    isBack: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = FirstPageRoute) {
        if (isBack) {
            popUpTo(FirstPageRoute) {
                inclusive = true
            }
        } else {
            navOptions()
        }
    }
}

fun NavController.navigateToSecondPage(
    isBack: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = SecondPageRoute) {
        if (isBack) {
            popUpTo(SecondPageRoute) {
                inclusive = true
            }
        } else {
            navOptions()
        }
    }
}

fun NavController.navigateToThirdPage(
    isBack: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ThirdPageRoute) {
        if (isBack) {
            popUpTo(ThirdPageRoute) {
                inclusive = true
            }
        } else {
            navOptions()
        }
    }
}

fun NavGraphBuilder.firstPage(
    modifier: Modifier = Modifier,
    sellerNameState: TextFieldState,
    businessOwnerState: TextFieldState,
    businessRegistrationNumberState: TextFieldState,
    telState: TextFieldState,
    sellerNameError: Boolean,
    businessOwnerError: Boolean,
    businessRegistrationNumberError: Boolean,
    telError: Boolean,
) {
    composable<FirstPageRoute> {
        FirstPage(
            modifier = modifier,
            sellerNameState = sellerNameState,
            businessOwnerState = businessOwnerState,
            businessRegistrationNumberState = businessRegistrationNumberState,
            telState = telState,
            sellerNameError = sellerNameError,
            businessOwnerError = businessOwnerError,
            businessRegistrationNumberError = businessRegistrationNumberError,
            telError = telError
        )
    }
}

fun NavGraphBuilder.secondPage(
    modifier: Modifier = Modifier,
    addressState: TextFieldState,
    descriptionState: TextFieldState,
    addressError: Boolean,
) {
    composable<SecondPageRoute> {
        SecondPage(
            modifier = modifier,
            addressState = addressState,
            descriptionState = descriptionState,
            addressError = addressError,
        )
    }
}

fun NavGraphBuilder.thirdPage(
    modifier: Modifier = Modifier,
    accountDepositorState: TextFieldState,
    accountNumberState: TextFieldState,
    accountDepositorError: Boolean,
    accountNumberError: Boolean,
) {
    composable<ThirdPageRoute> {
        ThirdPage(
            modifier = modifier,
            accountDepositorState = accountDepositorState,
            accountNumberState = accountNumberState,
            accountDepositorError = accountDepositorError,
            accountNumberError = accountNumberError,
        )
    }
}
