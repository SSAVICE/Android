package com.ssavice.user_main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ssavice.user_home.UserHomeViewModel
import com.ssavice.user_home.navigation.HomeRoute
import com.ssavice.user_home.navigation.home
import com.ssavice.user_my_page.ProfileState
import com.ssavice.user_my_page.navigation.myPage

@Composable
fun UserMainContainer(
    onSearch: () -> Unit = {},
    onServiceClick: (Long) -> Unit = {},

    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {}
) {
    val mainNavController = rememberNavController()
    val homeViewModel: UserHomeViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            MainBottomBar(mainNavController)
        },
        topBar = {
            MainTopBar(mainNavController, homeViewModel)
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            myPage(
                onEditProfileButtonClick = onEditProfileButtonClick,
                onParticipatedServiceButtonClick = onParticipatedServiceButtonClick,
                onLikedServiceButtonClick = onLikedServiceButtonClick,
                onHelpButtonClick = onHelpButtonClick,
                onLogoutButtonClick = onLogoutButtonClick,
                onWithdrawButtonClick = onWithdrawButtonClick,
            )

            home(
                onSearch = onSearch,
                onServiceClick = onServiceClick,
                viewModel = homeViewModel
            )
        }
    }
}
