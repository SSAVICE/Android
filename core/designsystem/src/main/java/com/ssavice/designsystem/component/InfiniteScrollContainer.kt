package com.ssavice.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.SsaviceTheme
import kotlinx.coroutines.delay

@Composable
fun InfiniteScrollContainer(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    isLoading: Boolean,
    hasMoreData: Boolean,
    onLoadMore: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    val isScrolledToEnd = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisibleItemIndex == layoutInfo.totalItemsCount - 1
            }
        }
    }

    LaunchedEffect(isScrolledToEnd.value) {
        if (isScrolledToEnd.value && !isLoading && hasMoreData) {
            onLoadMore()
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        content()

        if (isLoading || !hasMoreData) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(15.dp),
                            strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth * 0.7f
                        )
                    } else if (!hasMoreData) {
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            Text("마지막 상품입니다.")
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun InfiniteScrollContainerLoadingPreview() {
    SsaviceTheme{
        SsaviceBackground(modifier = Modifier.size(height=540.dp, width=360.dp)) {
            InfiniteScrollContainer(
                isLoading = true,
                hasMoreData = true,
                onLoadMore = {}
            ) {
                items(5) {
                    Text("Sample Item $it", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "No More Data State")
@Composable
private fun InfiniteScrollContainerNoMoreDataPreview() {
    SsaviceTheme{
        SsaviceBackground(modifier = Modifier.size(height=540.dp, width=360.dp)) {
            InfiniteScrollContainer(
                isLoading = false,
                hasMoreData = false,
                onLoadMore = {}
            ) {
                items(5) {
                    Text("Sample Item $it", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Scrollable Loading Preview")
@Composable
private fun InfiniteScrollContainerScrollablePreview() {
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(height=540.dp, width=360.dp)) {
            var isLoading by remember { mutableStateOf(false) }
            val items = remember { (1..20).toList() }

            LaunchedEffect(isLoading) {
                if (isLoading) {
                    delay(3000)
                    isLoading = false
                }
            }

            InfiniteScrollContainer(
                isLoading = isLoading,
                hasMoreData = true,
                onLoadMore = {
                    isLoading = true
                }
            ) {
                items(items) {
                    Text("Sample Item $it", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
