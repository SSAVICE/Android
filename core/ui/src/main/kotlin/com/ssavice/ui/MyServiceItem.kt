package com.ssavice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.enums.ServiceState

@Composable
fun MyService(
    modifier: Modifier = Modifier,
    state: ServiceState,
    title: String,
    sellerName: String,
    duration: String,
    cancellable: Boolean,
    reviewable: Boolean,
    price: String,
    thumbnailUrl: String,
    memberStatus: String? = null,
    onCancelButtonClick: () -> Unit = {},
    onReviewButtonClick: () -> Unit = {},
    thumbnail: @Composable (String) -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    SsaviceElevatedCard(
        modifier =
            modifier
                .padding(3.dp),
        onClick = onClick,
    )
    {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier =
                    Modifier
                        .weight(1f),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    thumbnail(thumbnailUrl)
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = sellerName,
                            style = MaterialTheme.typography.labelLarge,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        )
                        Spacer(Modifier.height(8.dp))
                        val style =
                            if (memberStatus != null) {
                                MaterialTheme.typography.bodyMedium
                            } else {
                                MaterialTheme.typography.bodyLarge
                            }
                        Text(
                            text = price,
                            style = style,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                        )
                        if (memberStatus != null) {
                            Spacer(Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Participants",
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.Gray,
                                )
                                Spacer(modifier = Modifier.width(1.dp))
                                ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                                    Text(
                                        text = memberStatus,
                                        fontSize = 11.sp,
                                        color = Color.Gray,
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = duration,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Clip,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }

            Column(
                modifier =
                    Modifier
                        .padding(top = 8.dp),
                horizontalAlignment = Alignment.End,
            ) {
                StatusChip(status = state)

                Spacer(Modifier.height(2.dp))
                if (cancellable) {
                    TextButton(
                        onClick = onCancelButtonClick,
                    ) {
                        Text(
                            text = "취소",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                if (reviewable) {
                    TextButton(
                        onClick = onReviewButtonClick,
                    ) {
                        Text(
                            text = "리뷰 작성",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(
    status: ServiceState,
    modifier: Modifier = Modifier,
) {
    val (bg, fg) =
        when (status) {
            ServiceState.RECRUITING -> Color(0xFFFFF3E0) to Color(0xFFE65100)

            ServiceState.SUCCEEDED,
            ServiceState.COMPLETED,
            ServiceState.ALL,
            -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)

            ServiceState.CANCELED,
            ServiceState.USER_CANCELED,
            -> Color(0xFFF5F5F5) to Color(0xFF757575)

            ServiceState.UNKNOWN,
            -> Color(0xFFFF8B8B) to Color(0xFFFF3434)
        }

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(999.dp))
                .background(bg)
                .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = status.value,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = fg,
        )
    }
}

@Preview
@Composable
private fun PreviewMyService() {
    SsaviceTheme {
        MyService(
            title = "요가 레슨",
            sellerName = "힐링요가스튜디오",
            duration = "2026-01-16 - 2026-02-03",
            cancellable = true,
            reviewable = true,
            price = "₩400,000",
            thumbnailUrl = YOGA_IMAGE,
            state = ServiceState.RECRUITING,
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
        ) {
        }
    }
}

@Preview
@Composable
private fun PreviewMyServiceLongTitle() {
    SsaviceTheme {
        MyService(
            title = "단순한 요가 레슨이 아니다 이건 엄청나게 긴 요가레슨으로",
            sellerName = "힐링요가스튜디오",
            duration = "2026-01-16 - 2026-02-03",
            cancellable = true,
            reviewable = false,
            price = "₩400,000",
            thumbnailUrl = YOGA_IMAGE,
            state = ServiceState.RECRUITING,
            memberStatus = "10/30 (최대 40)",
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
        ) {
        }
    }
}

private const val YOGA_IMAGE = ""
