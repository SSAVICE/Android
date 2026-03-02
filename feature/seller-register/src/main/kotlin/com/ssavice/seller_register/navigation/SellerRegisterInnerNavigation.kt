package com.ssavice.seller_register.navigation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.model.TimeStamp
import com.ssavice.seller_register.AddressForm
import com.ssavice.seller_register.ValidationState
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

val routeNumber =
    mapOf(
        1 to FirstPageRoute::class.qualifiedName,
        2 to SecondPageRoute::class.qualifiedName,
        3 to ThirdPageRoute::class.qualifiedName,
    )

fun NavController.navigateToFirstPage(
    isBack: Boolean,
    navOptions: NavOptionsBuilder.() -> Unit = {},
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
    navOptions: NavOptionsBuilder.() -> Unit = {},
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
    navOptions: NavOptionsBuilder.() -> Unit = {},
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
    businessOwnerState: TextFieldState,
    businessNameState: TextFieldState,
    businessRegistrationNumberState: TextFieldState,
    companyValidationState: ValidationState,
    companyOpenDate: TimeStamp,
    businessOwnerError: Boolean,
    businessNameError: Boolean,
    businessRegistrationNumberError: Boolean,
    onCompanyOpenDateChanged: (TimeStamp) -> Unit,
    onValidateButtonClicked: () -> Unit,
    tokenRemainingTime: Long,
) {
    composable<FirstPageRoute> {
        FirstPage(
            modifier = modifier,
            businessOwnerState = businessOwnerState,
            businessNameState = businessNameState,
            businessRegistrationNumberState = businessRegistrationNumberState,
            companyOpenDate = companyOpenDate,
            companyValidationState = companyValidationState,
            businessOwnerError = businessOwnerError,
            businessNameError = businessNameError,
            businessRegistrationNumberError = businessRegistrationNumberError,
            onCompanyOpenDateChanged = onCompanyOpenDateChanged,
            onValidateButtonClicked = onValidateButtonClicked,
            tokenRemainingTime = tokenRemainingTime,
        )
    }
}

fun NavGraphBuilder.secondPage(
    modifier: Modifier = Modifier,
    sellerNameState: TextFieldState,
    detailAddressState: TextFieldState,
    descriptionState: TextFieldState,
    telState: TextFieldState,
    addressError: Boolean,
    sellerNameError: Boolean,
    telError: Boolean,
    onAddressSelected: (AddressForm) -> Unit,
    addressState: AddressForm,
) {
    composable<SecondPageRoute> {
        SecondPage(
            modifier = modifier,
            detailAddressState = detailAddressState,
            descriptionState = descriptionState,
            sellerNameState = sellerNameState,
            telState = telState,
            addressError = addressError,
            sellerNameError = sellerNameError,
            telError = telError,
            onAddressSelected = onAddressSelected,
            addressState = addressState,
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
