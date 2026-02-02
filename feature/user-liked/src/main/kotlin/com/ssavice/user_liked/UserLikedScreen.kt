package com.ssavice.user_liked

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ssavice.designsystem.component.InfiniteScrollContainer
import com.ssavice.ui.ServiceListElement
import com.ssavice.ui.common.Constant
import kotlinx.coroutines.delay

@Composable
fun UserLikedRoute(
    modifier: Modifier = Modifier,
    viewModel: UserLikedViewModel = hiltViewModel(),
    onServiceClick: (Long) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.wishServiceScreenStatus) {
        if (state.wishServiceScreenStatus == WishServiceState.Initial) {
            delay(Constant.ANIMATION_DELAY)
            viewModel.loadService()
        }
    }

    UserLikedScreen(
        modifier = modifier,
        state = state,
        onServiceClick = onServiceClick,
        onRefresh = viewModel::loadMoreService,
    )
}

@Composable
fun UserLikedScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onServiceClick: (Long) -> Unit = {},
    state: UserLikedUiState,
    topElement: LazyListScope.() -> Unit = {},
) {
    val imageRequest =
        ImageRequest
            .Builder(LocalContext.current)
            .crossfade(true)
            .decoderFactory(SvgDecoder.Factory())
    InfiniteScrollContainer(
        modifier = modifier,
        onLoadMore = onRefresh,
        isLoading = state.wishServiceScreenStatus == WishServiceState.Loading
                || state.wishServiceScreenStatus == WishServiceState.Initial,
        hasMoreData = state.hasNext,
        topElement = topElement,
    ) {
        itemsIndexed(state.services) { index, item ->
            ServiceListElement(
                id = item.id,
                imageUrl = item.imageUrl,
                sellerName = item.sellerName,
                serviceName = item.serviceName,
                tags = item.tags,
                locationInfo = item.locationInfo,
                deadline = item.deadline,
                price = item.price,
                discountedPrice = item.discountedPrice,
                participationInfo = item.participationInfo,
                onServiceClick = onServiceClick,
                discountRate = item.discountRate,
            ) { url ->
                AsyncImage(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                    model =
                        imageRequest
                            .data(url)
                            .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }

            if (index < state.services.lastIndex) {
                HorizontalDivider(
                    Modifier.padding(horizontal = 10.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color.copy(alpha = 0.5f),
                )
            }
        }
    }
}
