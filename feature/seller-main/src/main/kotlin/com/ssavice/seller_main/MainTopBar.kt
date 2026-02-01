package com.ssavice.seller_main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssavice.seller_main.navigation.TopLevelDestination
import com.ssavice.ui.navigation.SsaviceTitle

@Composable
fun MainTopBar(
    navController: NavController) {
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
            SsaviceTitle(
                "대시보드"
            )
        }
        TopLevelDestination.CHATTING -> {
            SsaviceTitle(
                "채팅"
            )

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
