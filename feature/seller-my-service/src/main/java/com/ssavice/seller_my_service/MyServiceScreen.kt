package com.ssavice.seller_my_service

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.component.InfiniteScrollContainer
import com.ssavice.designsystem.component.SsaviceChip
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.enums.ServiceState
import com.ssavice.ui.MyService
import com.ssavice.ui.common.Constant
import kotlinx.coroutines.delay

@Composable
fun MyServiceRoute(
    modifier: Modifier = Modifier,
    viewModel: MyServiceViewModel = hiltViewModel(),
    onServiceClick: (Long) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.myServiceScreenStatus) {
        if (state.myServiceScreenStatus == MyServiceState.Initial) {
            delay(Constant.ANIMATION_DELAY)
            viewModel.loadService()
        }
    }

    MyServiceScreen(
        modifier = modifier,
        uiState = state,
        onLoadMore = viewModel::loadMoreService,
        onServiceClick = onServiceClick,
        onSearchingStateChanged = viewModel::onSearchingStateChange,
        onCancelClick = viewModel::onCancelClick,
    )
}

@Composable
fun MyServiceScreen(
    modifier: Modifier = Modifier,
    onServiceClick: (Long) -> Unit = {},
    onCancelClick: (Long) -> Unit = {},
    onSearchingStateChanged: (Int) -> Unit = {},
    onLoadMore: () -> Unit = {},
    uiState: SellerMyServiceUiState,
) {
    val isLoading =
        uiState.myServiceScreenStatus == MyServiceState.Loading ||
            uiState.myServiceScreenStatus == MyServiceState.Initial
    InfiniteScrollContainer(
        modifier =
            modifier
                .padding(horizontal = 15.dp)
                .padding(top = 10.dp),
        isLoading = isLoading,
        hasMoreData = uiState.hasNext && uiState.myServiceScreenStatus == MyServiceState.Loaded,
        onLoadMore = onLoadMore,
        topElement = {
            item {
                ServiceStateFilter(
                    modifier = Modifier.fillMaxWidth(),
                    searchRange = uiState.searchingState,
                    selection = uiState.searchTypeSelection,
                    onSelectionChanged = onSearchingStateChanged,
                )
                Spacer(Modifier.height(10.dp))
            }
        },
    ) {
        items(
            count = uiState.services.size,
            key = {
                uiState.services[it].index
            },
        ) {
            val service = uiState.services[it]
            MyService(
                title = service.title,
                sellerName = service.sellerName,
                duration = service.duration,
                cancellable = service.cancellable,
                reviewable = false,
                price = service.price,
                thumbnailUrl = service.thumbnailUrl,
                onCancelButtonClick = { onCancelClick(service.id) },
                onClick = { onServiceClick(uiState.services[it].id) },
                memberStatus = service.memberStatus,
                state = service.state,
                thumbnail = { url ->
                    AsyncImage(
                        model =
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(url)
                                .crossfade(true)
                                .build(),
                        contentDescription = "서비스 썸네일",
                        modifier =
                            Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp)),
                    )
                },
            )
        }
    }
}

@Composable
fun ServiceStateFilter(
    modifier: Modifier,
    searchRange: List<String>,
    selection: Int,
    spacing: Dp = 7.dp,
    onSelectionChanged: (Int) -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .horizontalScroll(rememberScrollState()),
        horizontalArrangement =
            androidx.compose.foundation.layout.Arrangement
                .spacedBy(spacing),
    ) {
        searchRange.forEachIndexed { i, range ->
            SsaviceChip(
                selected = i == selection,
                onSelectedChange = { onSelectionChanged(i) },
                text = range,
            )
        }
    }
}

@Preview
@Composable
fun MyServiceScreenPreview() {
    fun makeSampleData(i: Int): SellerMyServiceItemUiState =
        SellerMyServiceItemUiState(
            i,
            i.toLong(),
            "서비스 $i",
            "₩%,d".format(100000 * i),
            "https://picsum.photos/seed/item $i/200",
            "판매자 $i",
            "2026-01-16 - 2026-02-03",
            true,
            sellerId = 0,
            state = ServiceState.RECRUITING,
            memberStatus = "10/30 (최대 40)",
        )

    val state =
        SellerMyServiceUiState(
            services = (0..10).map { makeSampleData(it) },
            myServiceScreenStatus = MyServiceState.Loaded,
            hasNext = false,
            nextPage = 0,
            searchTypeSelection = 0,
        )

    SsaviceTheme {
        Scaffold { innerPadding ->
            MyServiceScreen(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                uiState = state,
                onSearchingStateChanged = {
                    state.copy(searchTypeSelection = it)
                },
            )
        }
    }
}
