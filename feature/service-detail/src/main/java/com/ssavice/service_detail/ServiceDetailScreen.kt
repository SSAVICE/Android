package com.ssavice.service_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.Date
import com.ssavice.ui.AsyncImageScrollList
import com.ssavice.ui.component.InfoRow

@Composable
fun ServiceDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ServiceDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onChatClick: (Long) -> Unit = {},
    onParticipateClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val serviceIdState by viewModel.serviceId.collectAsStateWithLifecycle()
    val sellerIdState by viewModel.sellerId.collectAsStateWithLifecycle()

    LaunchedEffect(serviceIdState) {
        if (serviceIdState != -1L) {
            viewModel.loadService(viewModel.serviceId.value)
        }
    }

    LaunchedEffect(sellerIdState) {
        if (sellerIdState != -1L) {
            viewModel.loadSeller(viewModel.sellerId.value)
        }
    }

    val enabled = uiState.serviceInfoState == InfoState.Done && uiState.sellerInfoState == InfoState.Done
    ServiceDetailScreen(
        modifier = modifier,
        uiState.service,
        uiState.seller,
        onBackClick = onBackClick,
        onChatClick = onChatClick,
        onParticipateClick = onParticipateClick,
        onLikeClick = onLikeClick,
        enabled = enabled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    modifier: Modifier = Modifier,
    service: ServiceDetail?,
    seller: SellerSummary?,
    onBackClick: () -> Unit = {},
    onChatClick: (Long) -> Unit = {},
    onParticipateClick: (Long) -> Unit = {},
    onLikeClick: (Long) -> Unit = {},
    enabled: Boolean = true,
) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) {
            item {
                Box {
                    AsyncImageScrollList(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                        imageUrls = service?.imageUrls ?: listOf(),
                        onImageClick = { /* TODO */ },
                    )
                    Row(
                        modifier =
                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp),
                    ) {
                        IconButton(
                            modifier =
                                Modifier
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = CircleShape,
                                        spotColor = DefaultShadowColor.copy(alpha = 0.4f),
                                        ambientColor = DefaultShadowColor.copy(alpha = 0.4f),
                                    ).clip(CircleShape),
                            onClick = { /* TODO */ },
                            colors =
                                IconButtonDefaults.filledIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                ),
                        ) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        IconButton(
                            modifier =
                                Modifier
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = CircleShape,
                                        spotColor = DefaultShadowColor.copy(alpha = 0.4f),
                                        ambientColor = DefaultShadowColor.copy(alpha = 0.4f),
                                    ).clip(CircleShape),
                            onClick = { /* TODO */ },
                            colors =
                                IconButtonDefaults.filledIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                ),
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        service?.name ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        service?.tags?.forEach { tag ->
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
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                ElevatedCard(
                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 2.dp,
                        ),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (service != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "₩%,d".format(service.discountedPrice),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "₩%,d".format(service.basePrice),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textDecoration = TextDecoration.LineThrough,
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "${service.discountRatio}% 할인",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        iconContentDescription = "Location",
                        title = "위치",
                        content = service?.address ?: "",
                    )
                    InfoRow(
                        icon = Icons.Default.Group,
                        iconContentDescription = "Participants",
                        title = "참여 인원",
                        content = service?.participantInfo ?: "",
                    )
                    InfoRow(
                        icon = Icons.Default.CalendarToday,
                        iconContentDescription = "Period",
                        title = "기간",
                        content = "${service?.startDate} ~ ${service?.endDate}",
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item { HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp)) }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(service?.description ?: "")
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                ElevatedCard(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    onClick = { /*TODO*/ },
                    colors =
                        CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // Company Logo
                            AsyncImage(
                                model =
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(seller?.imageUrl ?: "")
                                        .crossfade(true)
                                        .build(),
                                contentDescription = "Company Logo",
                                modifier =
                                    Modifier
                                        .size(48.dp),
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    seller?.name ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                    Spacer(modifier.width(3.dp))
                                    Text(
                                        "${seller?.rate ?: ""} (${seller?.rateCount}명)",
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                                Text(
                                    modifier = Modifier.padding(start = 3.dp),
                                    text = seller?.address ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = seller?.description ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "판매자 리뷰 (${seller?.rateCount})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { /*TODO*/ }) { Text("모두 보기") }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        seller?.reviews?.forEach { review ->
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
            }
        }

        Surface(shadowElevation = 8.dp) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    onClick = { /* onChatClick() */ },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("채팅하기")
                }
                Button(
                    onClick = { /* onParticipateClick() */ },
                    modifier = Modifier.weight(1f),
                ) {
                    Text("참여하기")
                }
            }
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
    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = userName,
                    fontWeight = FontWeight.Bold,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (index < rating) MaterialTheme.colorScheme.primary else Color.Gray,
                        )
                    }
                }
            }
            Text(serviceName, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(6.dp))
            Text(review)
        }
    }
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
                    "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?q=80&w=2120" +
                        "&auto=format&fit=crop&ixlib=rb-4.0.3" +
                        "&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                ),
            deadLine = "Date(2026,2,15)",
            id = 123123L,
            companyId = 123L,
            category = "건강",
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
                    Review(userName = "추*훈", content = "별로임", serviceName = "요가 클래스", createdAt = Date.now().toSimpleString(), rating = 2),
                ),
        )
    SsaviceTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            ServiceDetailScreen(
                modifier = Modifier.padding(innerPadding),
                service = service,
                seller = company,
            )
        }
    }
}
