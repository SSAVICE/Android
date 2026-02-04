package com.ssavice.seller_reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.component.InfiniteScrollContainer
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.ui.common.Constant
import kotlinx.coroutines.delay

@Composable
fun SellerReviewRoute(
    modifier: Modifier = Modifier,
    viewModel: SellerReviewsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.reviewState) {
        if (state.reviewState is SellerReviewState.Initial) {
            delay(Constant.ANIMATION_DELAY)
            viewModel.initiate()
        }
    }

    SellerReviewScreen(
        modifier = modifier,
        reviews = state.reviews,
        isLoading = state.reviewState is SellerReviewState.Loading
                || state.reviewState is SellerReviewState.Initial,
        hasMoreData = state.hasNext,
        onLoadMore = viewModel::loadMoreReviews,
    )
}

@Composable
fun SellerReviewScreen(
    modifier: Modifier = Modifier,
    reviews: List<SellerReviewItemState>,
    isLoading: Boolean,
    hasMoreData: Boolean,
    onLoadMore: () -> Unit,
) {
    InfiniteScrollContainer(
        modifier = modifier,
        isLoading = isLoading,
        hasMoreData = hasMoreData,
        onLoadMore = onLoadMore,
    ) {
        items(
            count = reviews.size,
            key = { reviews[it].index },
        ) {
            ReviewItem(
                review = reviews[it].comment,
                rating = reviews[it].rate,
                userName = reviews[it].userName,
                date = reviews[it].createdAt,
                serviceName = reviews[it].serviceName,
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun ReviewItem(
    review: String,
    rating: Int,
    userName: String,
    date: String,
    serviceName: String,
) {
    Column(modifier = Modifier.Companion.padding(16.dp)) {
        Row(verticalAlignment = Alignment.Companion.Top) {
            Text(
                modifier = Modifier.Companion.weight(1f),
                text = userName,
                fontWeight = FontWeight.Companion.Bold,
            )
            Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        modifier = Modifier.Companion.size(18.dp),
                        imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = if (index < rating) MaterialTheme.colorScheme.primary else Color.Companion.Gray,
                    )
                }
            }
        }
        Text(serviceName, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.Companion.height(4.dp))
        Text(
            date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        Spacer(modifier = Modifier.Companion.height(6.dp))
        Text(review)
    }
}

@Preview
@Composable
fun ReviewItemPreview() {
    val reviews by remember {
        derivedStateOf {
            (1..10).map {
                SellerReviewItemState(
                    index = it,
                    userName = "유저 $it",
                    comment = "리뷰 $it",
                    serviceName = "서비스 $it",
                    createdAt = "2023-09-01",
                    rate = it,
                )
            }
        }
    }

    SsaviceTheme {
        Scaffold { innerPadding ->
            SellerReviewScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                reviews = reviews,
                isLoading = false,
                hasMoreData = false,
                onLoadMore = {},
            )
        }
    }

}
