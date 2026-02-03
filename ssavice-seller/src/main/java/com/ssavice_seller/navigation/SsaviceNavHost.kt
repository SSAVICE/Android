package com.ssavice_seller.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ssavice.login.navigation.loginScreen
import com.ssavice.post_service.navigation.addServiceScreen
import com.ssavice.post_service.navigation.navigateToAddService
import com.ssavice.seller_edit_profile.navigation.editProfileScreen
import com.ssavice.seller_edit_profile.navigation.navigateToEditProfile
import com.ssavice.seller_home.navigation.navigateToHome
import com.ssavice.seller_main.navigation.mainScreen
import com.ssavice.seller_main.navigation.navigateToMain
import com.ssavice.seller_my_service.navigation.myServiceScreen
import com.ssavice.seller_my_service.navigation.navigateToMyService
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
                navController.navigateUp()
            },
            onSubmit = {
                navController.navigateUp()
            },
        )

        loginScreen(
            onLoginComplete = {
                navController.navigateToMain {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
            isUser = false,
        )

        mainScreen(
            onAddClick = {
                navController.navigateToAddService()
            },
            onParticipatedServiceButtonClick = {
                navController.navigateToMyService()
            },
            onEditProfileButtonClick = {
                navController.navigateToEditProfile(
                    name = it?.name,
                    description = it?.description,
                    detail = it?.detail,
                    phoneNumber = it?.phoneNumber,
                    profileImageUrl = it?.profileUrl,
                )
            },
        )

        myServiceScreen(
            onServiceClick = { _ ->
            },
            onBack = {
                navController.navigateUp()
            },
        )

        editProfileScreen(
            onSubmit = {
                navController.navigateUp()
            },
            onBackClick = {
                navController.navigateUp()
            },
        )
    }
}
