package com.ssavice.chat.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.chat.ChatBottomBar
import com.ssavice.chat.ChatRoute
import com.ssavice.chat.ChattingViewModel
import com.ssavice.designsystem.component.SsavicePopUpTopBar
import kotlinx.serialization.Serializable

@Serializable
data class ChattingRoomRoute(
    val roomId: String,
)

@Serializable
data class OnePerOneChatRoute(
    val userId: Long,
    val serviceId: Long? = null,
)

fun NavController.navigateToChattingRoom(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    roomId: String,
) {
    navigate(ChattingRoomRoute(roomId)) {
        navOptions()
    }
}

fun NavController.navigateToOnePerOneChattingRoom(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    userId: Long,
    serviceId: Long,
) {
    navigate(OnePerOneChatRoute(userId, serviceId)) {
        navOptions()
    }
}

fun NavGraphBuilder.chattingRoom(onBack: () -> Unit = {}) {
    composable<ChattingRoomRoute>(
        popEnterTransition = null,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(),
            )
        },
    ) {
        val viewModel: ChattingViewModel = hiltViewModel()

        Scaffold(
            topBar = {
                SsavicePopUpTopBar(
                    title = "채팅",
                    onBackClicked = onBack,
                )
            },
            bottomBar = {
                ChatBottomBar(
                    viewModel = viewModel,
                )
            },
        ) { innerPadding ->
            ChatRoute(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                viewModel = viewModel,
            )
        }
    }

    composable<OnePerOneChatRoute>(
        popEnterTransition = null,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(),
            )
        },
    ) { innerPadding ->
        val viewModel: ChattingViewModel = hiltViewModel()

        Scaffold(
            topBar = {
                SsavicePopUpTopBar(
                    title = "1:1 채팅",
                    onBackClicked = onBack,
                )
            },
            bottomBar = {
                ChatBottomBar(
                    viewModel = viewModel,
                )
            },
        ) { innerPadding ->
            ChatRoute(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                viewModel = viewModel,
            )
        }
    }
}

object ChatRouteContract {
    const val ROOM_ID = "roomId"
    const val USER_ID = "userId"
    const val SERVICE_ID = "serviceId"
}
