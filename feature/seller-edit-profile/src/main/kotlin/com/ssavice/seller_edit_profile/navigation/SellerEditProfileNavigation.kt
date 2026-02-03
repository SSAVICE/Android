package com.ssavice.seller_edit_profile.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import com.ssavice.designsystem.component.SsavicePopUpTopBar
import com.ssavice.seller_edit_profile.EditProfileRoute
import kotlinx.serialization.Serializable

@Serializable
data class EditSellerProfileRoute(
    val name: String = "",
    val description: String = "",
    val detail: String = "",
    val phoneNumber: String = "",
    val profileImageUrl: String = "",
)

fun NavController.navigateToEditProfile(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    name: String? = null,
    description: String? = null,
    detail: String? = null,
    phoneNumber: String? = null,
    profileImageUrl: String? = null,
) {
    val route: EditSellerProfileRoute =
        if (name == null || detail == null || description == null || phoneNumber == null || profileImageUrl == null) {
            EditSellerProfileRoute()
        } else {
            EditSellerProfileRoute(
                name = name,
                description = description,
                detail = detail,
                phoneNumber = phoneNumber,
                profileImageUrl = profileImageUrl,
            )
        }
    navigate(route = route) {
        navOptions()
    }
}

fun NavGraphBuilder.editProfileScreen(
    onSubmit: () -> Unit = {},
    onBackClick: () -> Unit,
    onProfileImageClick: () -> Unit = {},
) {
    composable<EditSellerProfileRoute>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(),
            )
        },
    )
    {
        Scaffold(
            topBar = {
                SsavicePopUpTopBar(
                    title = "프로필 수정",
                    onBackClicked = onBackClick,
                )
            },
        ) { innerPadding ->
            EditProfileRoute(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                viewModel = hiltViewModel(),
                onSubmit = onSubmit,
                onBackClick = onBackClick,
                onProfileImageClick = onProfileImageClick,
            )
        }
    }
}

object EditProfileRouteContract {
    const val NAME = "name"
    const val DESCRIPTION = "description"
    const val DETAIL = "detail"
    const val PHONE_NUMBER = "phoneNumber"
    const val IMAGE_URL = "profileImageUrl"
}
