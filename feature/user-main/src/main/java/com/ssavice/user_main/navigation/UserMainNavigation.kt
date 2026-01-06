package com.ssavice.user_main.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_main.UserMainScreen
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
    onScreenResolved: () -> Unit,
) {
    composable<MainRoute> {
        onScreenResolved()
        UserMainScreen(
            onSearchBarClicked = onSearch,
            onServiceClick = onServiceClick
        )
    }
}
