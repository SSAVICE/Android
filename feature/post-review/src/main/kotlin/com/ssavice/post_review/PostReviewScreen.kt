package com.ssavice.post_review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme

@Composable
fun PostReviewRoute(
    modifier: Modifier = Modifier,
    viewModel: PostReviewViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(
        state.reviewPostState,
    ) {
        when (state.reviewPostState) {
            is ReviewPostState.Failure -> {
                onBack()
            }

            ReviewPostState.Idle -> {
                viewModel.init()
            }

            ReviewPostState.Success -> {
                onBack()
            }

            else -> {}
        }
    }
    PostReviewScreen(
        modifier = modifier,
        productName = state.serviceName,
        productImageUrl = state.serviceThumbnailUrl,
        onReviewSubmit = { rating, review ->
            viewModel.postReview(state.serviceId,
                rating,
                review,
                state.sellerId)
        },
        canSubmit = state.reviewPostState is ReviewPostState.Idle,
    )
}

@Composable
fun PostReviewScreen(
    productName: String,
    productImageUrl: String,
    canSubmit: Boolean,
    onReviewSubmit: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reviewText = rememberTextFieldState()
    var rating by remember { mutableIntStateOf(0) }
    val submittable = (reviewText.text.isNotBlank() && rating > 0 && canSubmit)

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding(),
    ) {
        ProductItem(productName, productImageUrl)

        // 2. 별점 선택 영역
        Text(
            text = "상품은 어떠셨나요?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        StarRatingBar(
            rating = rating,
            onRatingChange = { rating = it },
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. 리뷰 작성 영역
        SsaviceInputField(
            state = reviewText,
            placeholderText = "상품에 대한 솔직한 리뷰를 남겨주세요.",
            multiLine = true,
            labelText = "내용",
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 4. 하단 작성 완료 버튼
        Button(
            onClick = { onReviewSubmit(rating, reviewText.text.toString()) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = submittable,
        ) {
            Text(
                text = "리뷰 작성 완료",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ProductItem(
    productName: String,
    productImageUrl: String,
    modifier: Modifier = Modifier,
) {
    SsaviceElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReviewProductThumbnail(imageUrl = productImageUrl)

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "상품명",
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
                Text(
                    text = productName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                )
            }
        }
    }
}

@Composable
fun ReviewProductThumbnail(imageUrl: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.size(60.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Product Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun StarRatingBar(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = modifier) {
        repeat(5) { index ->
            val starIndex = index + 1
            Icon(
                imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                contentDescription = "$starIndex Stars",
                tint = if (starIndex <= rating) MaterialTheme.colorScheme.primary else Color.LightGray,
                modifier =
                    Modifier
                        .size(40.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null, // Ripple 효과 제거
                            onClick = { onRatingChange(starIndex) },
                        ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostReviewScreenPreview() {
    SsaviceTheme {
        Scaffold { innerPadding ->
            PostReviewScreen(
                modifier = Modifier.padding(innerPadding),
                productName = "맛있는 사과 1kg",
                productImageUrl = "",
                onReviewSubmit = { _, _ -> },
                canSubmit = true,
            )
        }
    }
}
