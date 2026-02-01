package com.ssavice.seller_main

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ssavice.seller_main.navigation.TopLevelDestination
import com.ssavice.seller_main.navigation.navigateToTop

@Composable
fun MainBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val destinations =
        listOf(
            TopLevelDestination.USER_HOME,
            TopLevelDestination.CHATTING,
            TopLevelDestination.MY_PAGE,
        )

    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination?.route == destination.route::class.qualifiedName
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigateToTop(destination.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = destination.iconText,
                    )
                },
                label = { Text(destination.iconText) },
            )
        }
    }
}
