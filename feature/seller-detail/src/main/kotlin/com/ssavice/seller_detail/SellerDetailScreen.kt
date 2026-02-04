package com.ssavice.seller_detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ssavice.designsystem.component.SsavicePopUpTopBar
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.seller_detail.ui.ReviewItem
import com.ssavice.seller_detail.ui.SellerCard
import com.ssavice.ui.AsyncImageScrollList
import com.ssavice.ui.InfoRow
import com.ssavice.ui.SellerServiceListItem

@Composable
fun SellerDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: SellerDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.sellerDetailState) {
        if(state.sellerDetailState is SellerDetailState.Initial) {
            viewModel.load()
        }
    }

    if(state.sellerDetailState is SellerDetailState.Loaded) {
        SellerDetailScreen(
            modifier = modifier,
            state = state,
        )
    }
    else {
        Loading(400.dp)
    }
}

@Composable
fun SellerDetailScreen(
    modifier: Modifier = Modifier,
    state: SellerDetailUiState,
    scrollState: ScrollState = rememberScrollState()
) {
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        SellerServiceSummaryImages(imageUrls = state.sellerInfo.imageUrls)

        Spacer(Modifier.height(10.dp))

        SellerCard(
            name = "테스트 회사",
            thumbnailUrl = "https://picsum.photos/200",
            sellerRate = 4.12588,
            rateCount = 123,
            description = "테스트 회사입니다.",
        )

        Spacer(modifier = Modifier.height(16.dp))

        DescriptionAndAddress(
            detail = "테스트용 디테일" +
                    "\n이것은 두 번째 줄입니다." +
                    "\n디테일 정보는 많은 줄을 포함할 수 있어야 합니다.",
            address = "서울특별시 강남구 강남대로 10",
            phoneNumber = "010-1234-5678",
            detailAddress = "101동 1001호"
        )

        Spacer(modifier = Modifier.height(8.dp))

        ServiceSummary((1..3).map { demoService(it) })

        Spacer(modifier = Modifier.height(24.dp))

        ReviewSummary((1..3).map { demoReview(it) })

        Spacer(modifier = Modifier.height(24.dp))

        BusinessInfo(
            owner = "홍길동",
            phoneNumber = "010-1234-5678",
            businessNumber = "123-45-78910"
        )
    }
}

@Composable
private fun SellerServiceSummaryImages(imageUrls: List<String>) {
    AsyncImageScrollList(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        imageUrls = imageUrls,
    )
}

@Composable
private fun DescriptionAndAddress(
    detail: String,
    address: String,
    phoneNumber: String,
    detailAddress: String,
    onAddressClick: () -> Unit = {},
    onPhoneClick: () -> Unit = {},
) {
    Column(modifier = Modifier.padding(16.dp)) {

        // 상세 정보(주소/연락처) 섹션
        Text(
            text = "상세 정보",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            // 주소 항목
            InfoRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = true, onClick = onAddressClick),
                icon = Icons.Default.LocationOn,
                iconContentDescription = null,
                title = "주소",
                content = "$address\n$detailAddress"
            )

            // 구분선
            HorizontalDivider(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // 전화번호 항목
            InfoRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = true, onClick = onPhoneClick),
                icon = Icons.Default.Phone,
                iconContentDescription = null,
                title = "문의처",
                content = phoneNumber,
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 판매자 세부 소개 섹션
        Text(
            text = "판매자 소개",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 4.dp),
            lineHeight = 22.sp // 가독성을 위한 줄간격
        )
    }
}

@Composable
fun BusinessInfo(
    owner: String,
    phoneNumber: String,
    businessNumber: String
) {
    val spacing = 8.dp
    val labelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "사업자 정보",
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
           Column(
               modifier = Modifier.weight(1f),
               verticalArrangement = spacedBy(spacing)
           ) {
               ProvideTextStyle(
                   value = MaterialTheme.typography.labelMedium
               ) {
                   Text(
                       text = "대표자",
                       modifier = Modifier.fillMaxWidth(),
                       color = labelColor
                   )
                   Text(
                       text = "연락처",
                       modifier = Modifier.fillMaxWidth(),
                       color = labelColor
                   )
                   Text(
                       text = "사업자번호",
                       modifier = Modifier.fillMaxWidth(),
                       color = labelColor
                   )
               }
           }
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = spacedBy(spacing)
            ) {
                ProvideTextStyle(
                    value = MaterialTheme.typography.labelMedium
                ) {
                    Text(
                        text = owner,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = phoneNumber,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Text(
                        text = businessNumber,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceSummary(
    services: List<ServiceItemState>,
    onMoreClick: () -> Unit = {},
    onClickService: (Long) -> Unit = {},
    thumbnail: @Composable (String, ImageRequest.Builder) -> Unit = { url, request ->
        AsyncImage(
            model =
                request
                    .data(url)
                    .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
        )
    }
) {
    val imageRequest =
        ImageRequest
            .Builder(LocalContext.current)
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        LabelWithMoreButton(
            "서비스 목록",
            onMoreClick
        )
        Column {
            services.forEach {
                SellerServiceListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    title = it.name,
                    category = it.category,
                    meta = "",
                    priceText = it.price,
                    status = it.serviceState,
                    elevation = 2.dp,
                    onClick = { _ ->
                        onClickService(it.serviceId)
                    },
                ) {
                    thumbnail(it.thumbnailUrl, imageRequest)
                }
            }
        }
    }
}

@Composable
private fun ReviewSummary(
    reviews: List<ReviewItemState>,
    onMoreClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        LabelWithMoreButton(
            "판매자 리뷰",
            onMoreClick
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = spacedBy(5.dp)
        ) {
            reviews.forEach { review ->
                ReviewItem(
                    userName = review.userName,
                    review = review.comment,
                    serviceName = review.serviceName,
                    date = review.createdAt,
                    rating = review.rate
                )
            }
        }
    }
}

@Composable
private fun LabelWithMoreButton(
    label: String,
    onMoreClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.weight(1f))

        TextButton(
            onClick = onMoreClick,
        ) {
            Text(
                text = "더보기",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun Loading(height: Dp) {
    Box(
        modifier = Modifier.height(height).fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}


@Preview
@Composable
fun SellerCardPreview() {
    val sellerInfo = SellerInfoState(
        description = "테스트 회사입니다",
        name = "테스트 회사",
        detail = "테스트용 디테일" +
                "\n이것은 두 번째 줄입니다." +
                "\n디테일 정보는 많은 줄을 포함할 수 있어야 합니다.",
        address = "서울특별시 강남구 강남대로 10",
        detailAddress = "101동 1001호",
        phoneNumber = "010-1234-5678",
        imageUrls = listOf(
            "https://picsum.photos/id/122/200",
            "https://picsum.photos/id/123/200",
            "https://picsum.photos/id/124/200",
        ),
        id = 1L,
    )
    val services= (1..3).map {
        demoService(it)
    }
    val reviews = (1..3).map {
        demoReview(it)
    }
    val state by remember {
        mutableStateOf(
            SellerDetailUiState(
                sellerInfo = sellerInfo,
                serviceItems = services,
                reviewItems = reviews,
                sellerDetailState = SellerDetailState.Loaded
            )
        )
    }

    SsaviceTheme {
        Scaffold(
            topBar = { SsavicePopUpTopBar("판매자 상세 정보") },
        ) { innerPadding ->
            SellerDetailScreen(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                state = state
            )
        }
    }
}
