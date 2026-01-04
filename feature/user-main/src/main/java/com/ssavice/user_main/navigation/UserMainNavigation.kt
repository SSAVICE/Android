package com.ssavice.user_main.navigation

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

fun NavGraphBuilder.mainScreen(onSearch: () -> Unit = {}) {
    composable<MainRoute> {
        UserMainScreen(
            onSearchBarClicked = onSearch,
        )
    }
}
