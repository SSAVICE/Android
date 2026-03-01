package com.ssavice.service_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.kakaomap.KakaoMapDialog
import com.ssavice.model.Date
import com.ssavice.service_detail.ui.ServiceImagesWithButtons
import com.ssavice.service_detail.ui.seller.ParticipantUiModel
import com.ssavice.service_detail.ui.seller.SellerManageScreen
import com.ssavice.service_detail.ui.user.CompanyCard
import com.ssavice.service_detail.ui.user.ReviewItem
import com.ssavice.ui.InfoRow
import com.ssavice.ui.common.Constant
import kotlinx.coroutines.delay

@Composable
fun ServiceDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ServiceDetailViewModel = hiltViewModel(),
    onChatClick: (Long) -> Unit = {},
    onParticipateClick: (Long) -> Unit = {},
    onSellerClick: (Long) -> Unit = {},
    onMoreReviewClick: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sellerIdState by viewModel.sellerId.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.serviceInfoState) {
        if (uiState.serviceInfoState is InfoState.Initial) {
            delay(Constant.ANIMATION_DELAY)
            viewModel.onInit()
        }
        if(uiState.serviceInfoState is InfoState.StartChat) {
            onChatClick((uiState.serviceInfoState as InfoState.StartChat).id)
        }
    }

    LaunchedEffect(sellerIdState) {
        if (sellerIdState != -1L && uiState.sellerInfoState is InfoState.Initial) {
            viewModel.loadSeller(viewModel.sellerId.value)
        }
    }

    ServiceDetailScreen(
        modifier =
            modifier
                .verticalScroll(rememberScrollState()),
        uiState = uiState,
        onLikeClick = viewModel::onLikeButtonClick,
        onSellerClick = onSellerClick,
        onMoreReviewClick = onMoreReviewClick,
        onChatToUserClick = viewModel::onChatToUserButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    modifier: Modifier = Modifier,
    uiState: ServiceDetailUiState,
    onLikeClick: (Long) -> Unit = {},
    onSellerClick: (Long) -> Unit = {},
    onMoreReviewClick: (Long) -> Unit = {},
    onChatToUserClick: (Long) -> Unit = {}
) {
    val enabled =
        uiState.serviceInfoState == InfoState.Done && uiState.sellerInfoState == InfoState.Done

    val service = uiState.service

    Column(
        modifier =
        modifier,
    ) {
        ServiceDetailScreen(
            service = service,
            onLikeClick = onLikeClick,
            enabled = enabled,
            showLikeAndShare = uiState.showUserInfo,
        )

        if (uiState.showUserInfo) {
            val seller = uiState.seller
            if (seller != null && service != null) {
                SellerAndReviewScreen(
                    seller = seller,
                    sellerId = service.companyId,
                    onSellerClick = onSellerClick,
                    onMoreReviewClick = onMoreReviewClick,
                )
            } else {
                Loading(400.dp)
            }
        }

        if (uiState.showSellerInfo) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.LightGray.copy(alpha = 0.7f),
            )

            Spacer(Modifier.height(16.dp))

            val account = uiState.accountInfo
            if (account != null) {
                SellerManageScreen(
                    expectedRevenue = account.expectedRevenue,
                    participantCount = account.participantCount,
                    pricePerPerson = account.pricePerPerson,
                    lastNotice = account.lastNotice,
                    noticeDate = account.noticeDate,
                    participants = account.participants,
                    onChatClick = {onChatToUserClick(it.userId)}
                )
            } else {
                Loading(400.dp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    modifier: Modifier = Modifier,
    service: ServiceDetail?,
    showLikeAndShare: Boolean = true,
    onLikeClick: (Long) -> Unit = {},
    enabled: Boolean = true,
) {
    var showMap by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        if (service != null) {
            ServiceImagesWithButtons(
                urls = service.imageUrls,
                onLikeClick = { onLikeClick(service.id) },
                liked = service.liked,
                showButtons = showLikeAndShare,
            )

            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    service.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    service.tags.forEach { tag ->
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            SsaviceElevatedCard(
                modifier =
                    Modifier.Companion
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
            ) {
                Column(modifier = Modifier.Companion.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                        Text(
                            "₩%,d".format(service.discountedPrice),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Companion.Bold,
                        )
                        Spacer(modifier = Modifier.Companion.width(8.dp))
                        Text(
                            "₩%,d".format(service.basePrice),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textDecoration = TextDecoration.Companion.LineThrough,
                        )
                    }
                    Spacer(modifier = Modifier.Companion.height(4.dp))
                    Text(
                        "${service.discountRatio}% 할인",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Companion.Bold,
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                InfoRow(
                    icon = Icons.Default.LocationOn,
                    iconContentDescription = "Location",
                    title = "위치",
                    content = service.address,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable(enabled = true, onClick = { showMap = true }),
                )
                InfoRow(
                    icon = Icons.Default.Group,
                    iconContentDescription = "Participants",
                    title = "참여 인원",
                    content = service.participantInfo,
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )
                InfoRow(
                    icon = Icons.Default.CalendarToday,
                    iconContentDescription = "Period",
                    title = "기간",
                    content = "${service.startDate} ~ ${service.endDate}",
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.LightGray.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(service.description)
            }

            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Loading(400.dp)
        }
    }

    if (showMap && service != null) {
        KakaoMapDialog(
            onDismiss = { showMap = false },
            latitude = service.latitude,
            longitude = service.longitude,
            label = service.name,
        )
    }
}

@Composable
fun SellerAndReviewScreen(
    modifier: Modifier = Modifier,
    seller: SellerSummary,
    sellerId: Long,
    onSellerClick: (Long) -> Unit = {},
    onMoreReviewClick: (Long) -> Unit = {},
) {
    CompanyCard(
        seller = seller,
        onSellerClick = { onSellerClick(seller.id) },
    )

    Spacer(modifier = Modifier.height(24.dp))

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "판매자 리뷰 (${seller.rateCount})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { onMoreReviewClick(sellerId) }) { Text("모두 보기") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            seller.reviews.forEach { review ->
                ReviewItem(
                    review = review.content,
                    rating = review.rating,
                    userName = review.userName,
                    date = review.createdAt,
                    serviceName = review.serviceName,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Preview(showBackground = true)
@Composable
fun ServiceDetailScreenPreview() {
    val service =
        ServiceDetail(
            name = "주말 요가 클래스",
            tags = listOf("요가", "힐링", "운동"),
            basePrice = 70000,
            discountedPrice = 50000,
            discountRatio = 30,
            address = "강남구",
            participantInfo = "8/15명 (7자리 남음)",
            startDate = "2025-01-15",
            endDate = "2025-02-15",
            description = "초보자 친화적인 주말 요가 클래스입니다. 함께 건강하고 행복한 삶을 만들어봐요.",
            imageUrls =
                listOf(
                    "https://picsum.photos/seed/abc/1200/800",
                    "https://picsum.photos/seed/def/1200/800",
                    "https://picsum.photos/seed/ghq/1200/800",
                ),
            deadLine = "Date(2026,2,15)",
            id = 123123L,
            companyId = 123L,
            category = "건강",
            liked = true,
            applied = false,
            longitude = 127.0,
            latitude = 37.0,
        )
    val company =
        SellerSummary(
            name = "요가스튜디오 젠",
            rate = 4.8,
            rateCount = 156,
            address = "강남구 테헤란로",
            id = 123,
            description = "요가 요가",
            phoneNumber = "010-4067-8234",
            imageUrl = "",
            reviews =
                listOf(
                    Review(
                        userName = "권*찬",
                        content = "너무 좋아요",
                        serviceName = "요가 클래스",
                        createdAt = Date.now().toSimpleString(),
                        rating = 4,
                    ),
                    Review(
                        userName = "장*욱",
                        content = "사장님이 친절해요 \n서비스 퀄리티도 좋아요",
                        serviceName = "요가 클래스",
                        createdAt = Date.now().toSimpleString(),
                        rating = 5,
                    ),
                    Review(
                        userName = "추*훈",
                        content = "별로임",
                        serviceName = "요가 클래스",
                        createdAt = Date.now().toSimpleString(),
                        rating = 2,
                    ),
                ),
        )

    val uiState =
        ServiceDetailUiState(
            service = service,
            seller = company,
            showUserInfo = true,
        )
    SsaviceTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            ServiceDetailScreen(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
                uiState = uiState,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SellerServiceDetailScreenPreview() {
    val service =
        ServiceDetail(
            name = "주말 요가 클래스",
            tags = listOf("요가", "힐링", "운동"),
            basePrice = 70000,
            discountedPrice = 50000,
            discountRatio = 30,
            address = "강남구",
            participantInfo = "8/15명 (7자리 남음)",
            startDate = "2025-01-15",
            endDate = "2025-02-15",
            description = "초보자 친화적인 주말 요가 클래스입니다. 함께 건강하고 행복한 삶을 만들어봐요.",
            imageUrls =
                listOf(
                    "https://picsum.photos/seed/abc/1200/800",
                    "https://picsum.photos/seed/def/1200/800",
                    "https://picsum.photos/seed/ghq/1200/800",
                ),
            deadLine = "Date(2026,2,15)",
            id = 123123L,
            companyId = 123L,
            category = "건강",
            liked = true,
            applied = false,
            longitude = 127.0,
            latitude = 37.0,
        )

    val dummyParticipants =
        listOf(
            ParticipantUiModel(
                profileUrl = "https://picsum.photos/id/112/200",
                userId = 101,
                name = "김민수",
            ),
            ParticipantUiModel(
                profileUrl = "https://picsum.photos/id/113/200",
                userId = 102,
                name = "이서연",
            ),
            ParticipantUiModel(
                profileUrl = "https://picsum.photos/id/114/200",
                userId = 103,
                name = "박지훈",
            ),
            ParticipantUiModel(
                profileUrl = "https://picsum.photos/id/115/200",
                userId = 104,
                name = "최유나",
            ),
        )

    val uiState =
        ServiceDetailUiState(
            service = service,
            seller = null,
            accountInfo =
                SellerAccountInfo(
                    participants = dummyParticipants,
                    expectedRevenue = 1300000L,
                    participantCount = dummyParticipants.size,
                    pricePerPerson = 325000L,
                    lastNotice = "2/15 집합 장소가 변경되었습니다",
                    noticeDate = "02.08",
                ),
            showSellerInfo = true,
            showUserInfo = false,
        )
    SsaviceTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            ServiceDetailScreen(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState()),
                uiState = uiState,
            )
        }
    }
}

@Composable
fun Loading(height: Dp) {
    Box(
        modifier =
            Modifier
                .height(height)
                .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
