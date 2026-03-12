package com.ssavice.chat.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.chat.ChatBottomBar
import com.ssavice.chat.ChatRoute
import com.ssavice.chat.ChattingViewModel
import com.ssavice.chat.ui.ChatTopBar
import com.ssavice.chat.ui.ParticipantListSideBar
import com.ssavice.designsystem.component.SsavicePopUpTopBar
import kotlinx.coroutines.launch
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
        launchSingleTop = true
        popUpTo<ChattingRoomRoute> { inclusive = true }
        popUpTo<OnePerOneChatRoute> { inclusive = true }
        navOptions()
    }
}

fun NavController.navigateToOnePerOneChattingRoom(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    userId: Long,
    serviceId: Long,
) {
    navigate(OnePerOneChatRoute(userId, serviceId)) {
        launchSingleTop = true
        popUpTo<ChattingRoomRoute> { inclusive = true }
        popUpTo<OnePerOneChatRoute> { inclusive = true }
        navOptions()
    }
}

fun NavGraphBuilder.chattingRoom(
    onBack: () -> Unit = {},
    onServiceClick: (serviceId: Long) -> Unit = {},
) {
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
        ChattingRoomNavigationRoute(
            onBack = onBack,
            onServiceClick = onServiceClick,
        )
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
    ) {
        ChattingRoomNavigationRoute(
            onBack = onBack,
            onServiceClick = onServiceClick,
        )
    }
}

@Composable
private fun ChattingRoomNavigationRoute(
    onBack: () -> Unit = {},
    onServiceClick: (serviceId: Long) -> Unit = {},
) {
    val viewModel: ChattingViewModel = hiltViewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        drawerState.snapTo(DrawerValue.Closed)
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            scrimColor = DrawerDefaults.scrimColor,
            drawerContent = {
                ParticipantListSideBar(
                    viewModel = viewModel,
                )
            },
            gesturesEnabled = drawerState.isOpen,
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(
                    topBar = {
                        ChatTopBar(
                            defaultTitle = "새 채팅",
                            viewModel = viewModel,
                            onBack = onBack,
                            onDrawerOpenClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
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
                        onServiceClick = onServiceClick,
                    )
                }
            }
        }
    }
}

object ChatRouteContract {
    const val ROOM_ID = "roomId"
    const val USER_ID = "userId"
    const val SERVICE_ID = "serviceId"
}
