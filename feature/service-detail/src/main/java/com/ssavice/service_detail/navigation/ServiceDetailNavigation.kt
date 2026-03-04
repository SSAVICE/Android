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
    val sellerPage: Boolean = false,
)

fun NavController.navigateToServiceDetail(
    navOptions: NavOptionsBuilder.() -> Unit = {},
    isSeller: Boolean = true,
    serviceId: Long,
) {
    navigate(ServiceDetailRoute(serviceId, isSeller)) {
        navOptions()
    }
}

fun NavGraphBuilder.serviceDetailScreen(
    onBackClick: () -> Unit = {},
    onChatClick: (Long, Long) -> Unit = { _, _ -> },
    onParticipateClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    onSellerClick: (Long) -> Unit = {},
    onMoreReviewClick: (Long) -> Unit = {},
    onBack: () -> Unit = {},
) {
    composable<ServiceDetailRoute>(
        popEnterTransition = null,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(),
            )
        },
        popExitTransition = {
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
                onChatClick = onChatClick,
                onParticipateClick = onParticipateClick,
                onSellerClick = onSellerClick,
                onMoreReviewClick = onMoreReviewClick,
            )
        }
    }
}

object ServiceDetailRouteContract {
    const val ID = "serviceId"
    const val IS_SELLER = "sellerPage"
}
