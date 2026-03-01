package com.ssavice.seller_main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.seller_main.SellerMainContainer
import com.ssavice.seller_my_page.ProfileState
import kotlinx.serialization.Serializable

@Serializable
object MainRoute

fun NavController.navigateToMain(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(MainRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.mainScreen(
    onServiceClick: (Long) -> Unit = {},
    onAddClick: () -> Unit,
    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
    onChattingRoomClick: (roomId: String) -> Unit = {},
) {
    composable<MainRoute>
    {
        SellerMainContainer(
            onAddClick = onAddClick,
            onServiceClick = onServiceClick,
            onEditProfileButtonClick = onEditProfileButtonClick,
            onParticipatedServiceButtonClick = onParticipatedServiceButtonClick,
            onLikedServiceButtonClick = onLikedServiceButtonClick,
            onHelpButtonClick = onHelpButtonClick,
            onLogoutButtonClick = onLogoutButtonClick,
            onWithdrawButtonClick = onWithdrawButtonClick,
            onChattingRoomClick = onChattingRoomClick,
        )
    }
}
