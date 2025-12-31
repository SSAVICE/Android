package com.ssavice.ui.searchresult

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
fun SearchResultScreen(
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
            )
        }
    }
}
