package com.ssavice.service_detail.ui.seller

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.designsystem.theme.SsaviceTheme

data class ParticipantUiModel(
    val profileUrl: String,
    val userId: Long,
    val name: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerManageScreen(
    modifier: Modifier = Modifier,
    expectedRevenue: Long,
    participantCount: Int,
    pricePerPerson: Long,
    lastNotice: String?,
    noticeDate: String?,
    participants: List<ParticipantUiModel>, // 외부에서 정의된 모델 가정
    onNewNoticeClick: () -> Unit = {},
    onNoticeDetailClick: () -> Unit = {},
    onChatClick: (ParticipantUiModel) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .padding(horizontal = 16.dp),
    ) {
        // 1. 예상 수익 카드
        ManageSectionCard(modifier = Modifier.padding(top = 16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("예상 수익", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "₩${String.format("%,d", expectedRevenue)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Default.PersonOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = "${participantCount}명 x ₩${String.format("%,d", pricePerPerson)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // 2. 공지사항 카드
        ManageSectionCard {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "공지사항",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray,
                        )
                    }
                    Text(
                        text = "+ 새 공지 작성",
                        modifier = Modifier.clickable { onNewNoticeClick() },
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 공지 아이템 영역
                Surface(
                    onClick = onNoticeDetailClick,
                    color = Color(0xFFf6f6f6),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = lastNotice ?: "등록된 공지가 없습니다.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = noticeDate ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.LightGray,
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 24.dp),
            color = Color.LightGray.copy(alpha = 0.7f),
        )

        // 3. 참가자 명단 섹션
        SectionHeader(
            title = "참가자 명단",
            count = participantCount,
        )

        val builder =
            ImageRequest
                .Builder(LocalContext.current)
                .crossfade(true)

        participants.fastForEachIndexed { i, participant ->
            ParticipantItem(
                participant = participant,
                onChatClick = { onChatClick(participant) },
                builder,
            )

            if (i != participants.lastIndex) {
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.7f))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ParticipantItem(
    participant: ParticipantUiModel,
    onChatClick: () -> Unit,
    imageRequest: ImageRequest.Builder,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 프로필 이미지 (Coil 사용)
        AsyncImage(
            model =
                imageRequest
                    .data(participant.profileUrl)
                    .build(),
            contentDescription = null,
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = participant.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
        }

        // 채팅 버튼
        Row(
            modifier =
                Modifier
                    .clickable { onChatClick() }
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "채팅",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun ManageSectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    SsaviceElevatedCard(
        modifier = modifier.fillMaxWidth(),
        content = content,
    )
}

@Composable
fun SectionHeader(
    title: String,
    count: Int? = null,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            count?.let {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer, // 연한 핑크 배경
                    shape = CircleShape,
                ) {
                    Text(
                        text = "${it}명",
                        modifier =
                            Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 2.dp,
                            ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
        actionText?.let {
            Text(
                text = "+ $it",
                modifier = Modifier.clickable { onActionClick() },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Preview()
@Composable
private fun SellerManageScreenPreview() { // 1. 더미 데이터 생성
    val dummyParticipants =
        listOf(
            ParticipantUiModel(
                profileUrl = "https://example.com/p1.jpg",
                userId = 101,
                name = "김민수",
            ),
            ParticipantUiModel(
                profileUrl = "https://example.com/p2.jpg",
                userId = 102,
                name = "이서연",
            ),
            ParticipantUiModel(
                profileUrl = "https://example.com/p3.jpg",
                userId = 103,
                name = "박지훈",
            ),
            ParticipantUiModel(
                profileUrl = "https://example.com/p4.jpg",
                userId = 104,
                name = "최유나",
            ),
        )

    SsaviceTheme {
        Scaffold { innerPadding ->
            SellerManageScreen(
                modifier = Modifier.padding(innerPadding),
                expectedRevenue = 1300000L,
                participantCount = dummyParticipants.size,
                pricePerPerson = 325000L,
                lastNotice = "2/15 집합 장소가 변경되었습니다",
                noticeDate = "02.08",
                participants = dummyParticipants,
                onNewNoticeClick = {},
                onNoticeDetailClick = {},
                onChatClick = {},
            )
        }
    }
}
