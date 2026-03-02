package com.ssavice.user_main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_main.UserMainContainer
import com.ssavice.user_my_page.ProfileState
import kotlinx.serialization.Serializable

@Serializable
object MainRoute

fun NavController.navigateToMain(navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(MainRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.mainScreen(
    onSearch: () -> Unit = {},
    onServiceClick: (Long) -> Unit = {},
    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
    onServiceSummaryClick: (com.ssavice.model.enums.ServiceState) -> Unit = {},
    onChattingRoomClick: (roomId: String) -> Unit = {},
) {
    composable<MainRoute>
    {
        UserMainContainer(
            onSearch = onSearch,
            onServiceClick = onServiceClick,
            onEditProfileButtonClick = onEditProfileButtonClick,
            onParticipatedServiceButtonClick = onParticipatedServiceButtonClick,
            onLikedServiceButtonClick = onLikedServiceButtonClick,
            onHelpButtonClick = onHelpButtonClick,
            onLogoutButtonClick = onLogoutButtonClick,
            onWithdrawButtonClick = onWithdrawButtonClick,
            onServiceSummaryClick = onServiceSummaryClick,
            onChattingRoomClick = onChattingRoomClick,
        )
    }
}
