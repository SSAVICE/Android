package com.ssavice.ssavice.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ssavice.search.navigation.navigateToSearchForm
import com.ssavice.search.navigation.searchFormScreen
import com.ssavice.user_main.navigation.MainRoute
import com.ssavice.user_main.navigation.mainScreen

@Composable
fun SsaviceNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MainRoute,
        modifier = modifier,
    ) {
        mainScreen(
            onSearch = {
                navController.navigateToSearchForm ()
            }
        )
        searchFormScreen {

        }
    }
}
