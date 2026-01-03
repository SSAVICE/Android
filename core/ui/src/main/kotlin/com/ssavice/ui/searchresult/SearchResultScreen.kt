package com.ssavice.ui.searchresult

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchResultViewModel = hiltViewModel(),
    query: SearchQuery,
    onServiceClick: (Long) -> Unit = {},
    onItemCountChange: (Int) -> Unit = {},
    topElement: LazyListScope.() -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(query) {
        viewModel.newSearch(query)
    }

    LaunchedEffect(state.value.items.size) {
        onItemCountChange(state.value.items.size)
    }

    SearchResultScreen(
        modifier = modifier,
        state = state.value,
        onServiceClick = onServiceClick,
        onRefresh = viewModel::loadMoreItems,
        topElement = topElement
    )
}

@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onServiceClick: (Long) -> Unit = {},
    state: SearchResultUiState,
    topElement: LazyListScope.() -> Unit = {}
) {
    val imageRequest = ImageRequest
        .Builder(LocalContext.current)
        .crossfade(true)
        .decoderFactory(SvgDecoder.Factory())
    InfiniteScrollContainer(
        modifier = modifier,
        onLoadMore = onRefresh,
        isLoading = state.status == SearchStatus.Loading,
        hasMoreData = state.hasNext,
        topElement = topElement
    ) {
        itemsIndexed(state.items) { index, item ->
            ServiceListElement(
                id = item.id,
                imageUrl = item.imageUrl,
                sellerName = item.companyName,
                serviceName = item.name,
                tags = item.tag.split(',').toList(),
                locationInfo = item.address,
                deadline = item.deadLine,
                price = item.basePrice,
                discountedPrice = item.discountedPrice,
                participationInfo = item.memberStatus,
                onServiceClick = onServiceClick,
                discountRate = item.discountRatio
            ) { url ->
                AsyncImage(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                    model = imageRequest
                        .data(url)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }

            if (index < state.items.lastIndex) {
                HorizontalDivider(
                    Modifier.padding(horizontal = 10.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color.copy(alpha = 0.5f)
                )
            }
        }
    }
}
