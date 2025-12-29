package com.ssavice.seller_main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.ui.SellerServiceListItem
import com.ssavice.ui.ServiceStatus
import java.net.URL


@Composable
fun SellerMainScreen(
    modifier: Modifier = Modifier,
    viewModel: SellerMainViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SellerMainScreen(
        state = state,
        modifier = modifier,
        onAddClick = {}
    )
}

@Composable
fun SellerMainScreen(
    state: SellerMainUiState,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnail: @Composable (SellerItemUiState) -> Unit = {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it.imageUrl.toString())
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    },
    onServiceClick: (SellerItemUiState) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(
                value = MaterialTheme.typography.titleMedium
            ) {
                Text(
                    text = "등록된 서비스",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.weight(1f))

            SsaviceButton(
                onClick = onAddClick,
                text = "+ 추가"
            )
        }

        Spacer(Modifier.height(12.dp))

        // 리스트
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(
                items = if (state is SellerMainUiState.Shown) state.items else emptyList(),
                key = { it.id }
            ) { item ->
                // 클릭이 필요하면 카드에 clickable 추가하면 됨 (여기선 레이아웃 위주로 유지)
                SellerServiceListItem(
                    title = item.title,
                    category = item.category,
                    meta = item.meta,
                    priceText = item.priceText,
                    status = if (item.isRecruiting) ServiceStatus.IN_PROGRESS
                    else ServiceStatus.RECRUITING,
                    thumbnail = { thumbnail(item) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .shadow(8.dp, RoundedCornerShape(14.dp), clip = false)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                )
            }
        }
    }
}

@Preview
@Composable
fun SellerMainScreenPreview() {
    val items = remember {
        listOf(
            SellerItemUiState(
                0, "요가 레슨", "운동/피트니스", "15명",
                "₩400,000", true,
                URL(YOGA_PREVIEW)
            ),
            SellerItemUiState(
                1, "기초 영어 회화", "운동/학습", "12명",
                "₩400,000", true,
                URL(YOGA_PREVIEW)
            ),
            SellerItemUiState(
                2, "유기농 과자 공구", "운동/피트니스", "45명",
                "₩750,000", false,
                URL(YOGA_PREVIEW)
            )
        )
    }
    val state = SellerMainUiState.Shown(items)
    SsaviceTheme {
        SellerMainScreen(
            state = state,
            onAddClick = {},
            onServiceClick = {},
            thumbnail = {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.imageUrl.toString())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    }
}


private const val YOGA_PREVIEW =
    "https://images.unsplash.com/photo-1552196527-bffef41ef674?q=80&w=2226&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
