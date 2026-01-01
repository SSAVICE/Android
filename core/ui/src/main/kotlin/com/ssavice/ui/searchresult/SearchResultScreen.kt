package com.ssavice.ui.searchresult

import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.request.ImageRequest
import com.ssavice.designsystem.component.InfiniteScrollContainer
import com.ssavice.ui.ServiceListElement

@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchResultViewModel = hiltViewModel(),
    query: SearchQuery,
    onServiceClick: (Long) -> Unit = {},
    onItemCountChange: (Int) -> Unit = {}
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
        onRefresh = viewModel::loadMoreItems
    )
}

@Composable
internal fun SearchResultScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    onServiceClick: (Long) -> Unit = {},
    state: SearchResultUiState
) {
    InfiniteScrollContainer(
        modifier = modifier,
        onLoadMore = onRefresh,
        isLoading = state.status == SearchStatus.Loading,
        hasMoreData = state.hasNext,
    ) {
        items(state.items.size) {
            val item = state.items[it]
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
            ) {
                url ->
                AsyncImage(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                    model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}
