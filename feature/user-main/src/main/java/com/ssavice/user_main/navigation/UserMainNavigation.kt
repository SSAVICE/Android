package com.ssavice.user_main.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.user_main.UserMainScreen
import com.ssavice.user_main.UserMainViewModel
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
    onScreenResolved: (
        viewModel: UserMainViewModel,
    ) -> Unit,
) {
    composable<MainRoute> {
        val viewModel: UserMainViewModel = hiltViewModel()
        onScreenResolved(viewModel)
        UserMainScreen(
            viewModel = viewModel,
            onSearchBarClicked = onSearch,
            onServiceClick = onServiceClick,
        )
    }
}
