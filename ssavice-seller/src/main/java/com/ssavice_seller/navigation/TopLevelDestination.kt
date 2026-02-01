package com.ssavice_seller.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.ssavice.seller_home.navigation.HomeRoute
import kotlinx.serialization.Serializable

enum class TopLevelDestination(
    val route: @Serializable Any,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconText: String,
) {
    USER_MAIN(
        route = HomeRoute,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconText = "홈",
    ),
    CHATTING(
        route = HomeRoute,
        selectedIcon = Icons.Filled.ChatBubble,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        iconText = "채팅",
    ),
    MY_PAGE(
        route = HomeRoute,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.PersonOutline,
        iconText = "마이",
    ),
}

fun NavController.navigateToTop(route: @Serializable Any) =
    navigate(route) {
        graph.startDestinationRoute?.let { popUpTo(it) }
        launchSingleTop = true
        restoreState = true
    }
