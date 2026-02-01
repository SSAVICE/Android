package com.ssavice.service_detail.navigation

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
import com.ssavice.designsystem.component.SsavicePopUpTopBar
import com.ssavice.service_detail.ServiceDetailScreen
import com.ssavice.service_detail.ServiceDetailViewModel
import com.ssavice.service_detail.ui.ServiceDetailBottomBar
import kotlinx.serialization.Serializable

@Serializable
data class ServiceDetailRoute(
    val serviceId: Long,
)

fun NavController.navigateToServiceDetail(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    serviceId: Long,
) {
    navigate(ServiceDetailRoute(serviceId)) {
        navOptions()
    }
}

fun NavGraphBuilder.serviceDetailScreen(
    onBackClick: () -> Unit = {},
    onChatClick: (Long) -> Unit = {},
    onParticipateClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onBack: () -> Unit = {},
) {
    composable<ServiceDetailRoute>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(),
            )
        },
    ) {
        val viewModel: ServiceDetailViewModel = hiltViewModel()

        Scaffold(
            topBar = {
                SsavicePopUpTopBar(
                    title = "상세 정보",
                    onBackClicked = onBack,
                )
            },
            bottomBar = {
                ServiceDetailBottomBar(
                    viewModel = viewModel,
                )
            },
        ) { innerPadding ->

            ServiceDetailScreen(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                viewModel = viewModel,
                onBackClick = onBackClick,
                onChatClick = onChatClick,
                onParticipateClick = onParticipateClick,
                onLikeClick = onLikeClick,
            )
        }
    }
}

object ServiceDetailRouteContract {
    const val ID = "serviceId"
}
