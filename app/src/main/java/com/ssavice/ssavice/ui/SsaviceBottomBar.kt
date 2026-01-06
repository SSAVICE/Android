package com.ssavice.ssavice.ui

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ssavice.ssavice.navigation.TopLevelDestination
import com.ssavice.ssavice.navigation.navigateToTop

@Composable
fun SsaviceBottomBar(
    navController: NavController
) {
    val destinations =
        listOf(
            TopLevelDestination.USER_MAIN,
            TopLevelDestination.CHATTING,
            TopLevelDestination.MY_PAGE,
        )

    NavigationBar {
        destinations.forEach { destination ->
            val selected = navController.currentDestination?.route == destination.route::class.qualifiedName
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if(!selected) {
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
