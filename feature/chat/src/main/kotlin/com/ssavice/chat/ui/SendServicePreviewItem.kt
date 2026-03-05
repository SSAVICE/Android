package com.ssavice.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.chat.ServiceInfo
import com.ssavice.designsystem.theme.shimmerBrush
import com.ssavice.model.chat.ChattingServiceSummary

@Composable
fun SendServicePreviewItem(
    serviceInfo: ChattingServiceSummary?,
    imageRequestBuilder: ImageRequest.Builder,
    modifier: Modifier = Modifier.Companion,
) {
    val isDataLoading = serviceInfo == null
    val brush = shimmerBrush(showShimmer = isDataLoading)

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                    ),
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
    ) {
        Column(
            modifier =
                Modifier.Companion
                    .padding(12.dp)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
        ) {
            Text("다음 상품에 대해 물어보세요!")
            Spacer(Modifier.Companion.height(5.dp))
            Row(
                modifier = Modifier.Companion,
                verticalAlignment = Alignment.Companion.CenterVertically,
            ) {
                // 1. 상품 썸네일 영역
                if (isDataLoading) {
                    Box(
                        modifier =
                            Modifier.Companion
                                .size(48.dp)
                                .clip(
                                    androidx.compose.foundation.shape
                                        .RoundedCornerShape(8.dp),
                                ).background(brush),
                    )
                } else {
                    AsyncImage(
                        model = imageRequestBuilder.data(serviceInfo.serviceThumbnail).build(),
                        contentDescription = null,
                        modifier =
                            Modifier.Companion
                                .size(48.dp)
                                .clip(
                                    androidx.compose.foundation.shape
                                        .RoundedCornerShape(8.dp),
                                ),
                        contentScale = ContentScale.Companion.Crop,
                    )
                }

                Spacer(modifier = Modifier.Companion.width(12.dp))

                // 2. 정보 영역 (판매자, 제목, 가격)
                Column(
                    modifier = Modifier.Companion.weight(1f),
                ) {
                    if (isDataLoading) {
                        // Shimmering Placeholders
                        Box(
                            modifier =
                                Modifier.Companion
                                    .width(60.dp)
                                    .height(12.dp)
                                    .background(brush),
                        )
                        Spacer(modifier = Modifier.Companion.height(4.dp))
                        Box(
                            modifier =
                                Modifier.Companion
                                    .fillMaxWidth(0.7f)
                                    .height(16.dp)
                                    .background(brush),
                        )
                        Spacer(modifier = Modifier.Companion.height(4.dp))
                        Box(
                            modifier =
                                Modifier.Companion
                                    .width(80.dp)
                                    .height(14.dp)
                                    .background(brush),
                        )
                    } else {
                        serviceInfo?.let { info ->
                            Text(
                                text = info.serviceSeller,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Companion.Ellipsis,
                            )
                            Text(
                                text = info.serviceName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Companion.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Companion.Ellipsis,
                            )
                            Text(
                                text = "%,d원".format(info.servicePrice),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}
