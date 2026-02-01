package com.ssavice.user_main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssavice.designsystem.component.SsavicePopUpTopBar
import com.ssavice.designsystem.component.SsaviceTopBar
import com.ssavice.ui.navigation.SsaviceTitle
import com.ssavice.user_home.UserHomeTopBar
import com.ssavice.user_home.UserHomeViewModel
import com.ssavice.user_main.navigation.TopLevelDestination

@Composable
fun MainTopBar(
    navController: NavController,
    homeViewModel: UserHomeViewModel) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val destinations =
        listOf(
            TopLevelDestination.USER_HOME,
            TopLevelDestination.CHATTING,
            TopLevelDestination.MY_PAGE,
        )

    when(destinations.find { currentDestination?.route == it.route::class.qualifiedName }) {
        TopLevelDestination.USER_HOME -> {
            UserHomeTopBar(homeViewModel)
        }
        TopLevelDestination.CHATTING -> {

        }
        TopLevelDestination.MY_PAGE -> {
            SsaviceTitle(
                "마이 페이지"
            )
        }
        null -> {

        }
    }
}
