package com.ssavice.seller_main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ssavice.seller_home.navigation.HomeRoute
import com.ssavice.seller_home.navigation.home
import com.ssavice.seller_my_page.ProfileState
import com.ssavice.seller_my_page.navigation.myPage
import com.ssavice.ui.navigation.SsaviceScaffold
import com.ssavice.chat_list.navigation.chat

@Composable
fun SellerMainContainer(
    onServiceClick: (Long) -> Unit = {},
    onAddClick: () -> Unit,
    onEditProfileButtonClick: (ProfileState?) -> Unit = {},
    onParticipatedServiceButtonClick: () -> Unit = {},
    onLikedServiceButtonClick: () -> Unit = {},
    onHelpButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onWithdrawButtonClick: () -> Unit = {},
    onChattingRoomClick: (roomId: String) -> Unit = {},
) {
    val mainNavController = rememberNavController()
    SsaviceScaffold(
        bottomBar = {
            MainBottomBar(mainNavController)
        },
        topBar = {
            MainTopBar(mainNavController)
        },
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            myPage(
                onMyServiceClick = onParticipatedServiceButtonClick,
                onEditProfileClick = onEditProfileButtonClick,
            )

            home(
                onAddClick = onAddClick,
                onServiceClick = onServiceClick,
            )

            chat(
                onRoomClick = onChattingRoomClick,
            )
        }
    }
}
