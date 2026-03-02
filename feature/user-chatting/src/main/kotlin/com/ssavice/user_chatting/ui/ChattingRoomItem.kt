package com.ssavice.user_chatting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.theme.SsaviceTheme

@Composable
fun ChattingRoomItem(
    title: String,
    lastMessage: String,
    thumbnailUrl: String?,
    updatedAt: String,
    unreadCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    requestBuilder: ImageRequest.Builder
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(IntrinsicSize.Min), // 높이를 내부 요소에 맞춤
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. 좌측 썸네일
            AsyncImage(
                model = requestBuilder.data(thumbnailUrl).build(),
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 2. 중앙 정보 (Title & Last Message)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 3. 우측 정보 (Time & Unread Indicator)
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = updatedAt,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 2.dp)
                )

                if (unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                            .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary), // PrimaryColor 적용
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    // 읽지 않은 메시지가 없을 때 공간 확보를 위한 빈 공간
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun ChattingRoomItemPreview() {
    val context = androidx.compose.ui.platform.LocalContext.current

    // 프리뷰용 ImageRequest.Builder 생성
    val previewRequestBuilder = ImageRequest.Builder(context)
        .crossfade(true)
        .placeholder(android.R.drawable.ic_menu_info_details) // 샘플용 플레이스홀더

    SsaviceTheme {
        Column {
            // 1. 읽지 않은 메시지가 있는 경우
            ChattingRoomItem(
                title = "SSAVICE 프로젝트 팀",
                lastMessage = "오늘 회의는 오후 2시에 진행하도록 하겠습니다. 다들 확인 부탁드려요!",
                thumbnailUrl = "https://example.com/image1.jpg",
                updatedAt = "오후 2:30",
                unreadCount = 5,
                onClick = {},
                requestBuilder = previewRequestBuilder
            )

            androidx.compose.material3.HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // 2. 읽지 않은 메시지가 없는 경우
            ChattingRoomItem(
                title = "김철수",
                lastMessage = "넵 알겠습니다!",
                thumbnailUrl = null, // 썸네일이 없는 경우 테스트
                updatedAt = "어제",
                unreadCount = 0,
                onClick = {},
                requestBuilder = previewRequestBuilder
            )

            // 3. 메시지가 매우 길고 읽지 않은 알림이 99+인 경우
            ChattingRoomItem(
                title = "익명 단톡방 - 매우 긴 제목을 가진 채팅방의 경우 어떻게 보일까요?",
                lastMessage = "이 메시지는 엄청나게 길어서 한 줄로 표시되지 않고 말줄임표 처리가 되어야 합니다. 테스트를 위한 긴 텍스트입니다.",
                thumbnailUrl = "https://example.com/image2.jpg",
                updatedAt = "3월 2일",
                unreadCount = 150,
                onClick = {},
                requestBuilder = previewRequestBuilder
            )
        }
    }
}
