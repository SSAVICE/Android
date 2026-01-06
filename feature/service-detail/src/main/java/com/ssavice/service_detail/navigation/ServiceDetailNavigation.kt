package com.ssavice.service_detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ssavice.service_detail.ServiceDetailScreen
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
) {
    composable<ServiceDetailRoute> {
        ServiceDetailScreen(
            onBackClick = onBackClick,
            onChatClick = onChatClick,
            onParticipateClick = onParticipateClick,
            onLikeClick = onLikeClick,
        )
    }
}

object ServiceDetailRouteContract {
    const val ID = "serviceId"
}
