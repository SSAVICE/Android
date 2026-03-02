package com.ssavice.designsystem.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.designsystem.theme.shimmerBrush

enum class ChatBubbleType {
    SIMPLE, // 텍스트만 표시
    ALL,     // 프로필, 이름, 시간 포함 표시
}

enum class ChatBubbleDirection {
    SENT,// 내가 보낸 메시지 (오른쪽 정렬)
    RECEIVED // 상대방이 보낸 메시지 (왼쪽 정렬)
}

private val THUMBNAIL_SIZE = 40.dp
val allSentShape = RoundedCornerShape(
    topStart = 16.dp,
    topEnd = 16.dp,
    bottomStart = 16.dp,
    bottomEnd = 4.dp
)

val allReceivedShape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 16.dp,
    bottomStart = 16.dp,
    bottomEnd = 16.dp
)
val simpleShape = RoundedCornerShape(
    topStart = 16.dp,
    topEnd = 16.dp,
    bottomStart = 16.dp,
    bottomEnd = 16.dp
)

@Composable
fun ChatBubble(
    message: String,
    userName: String? = null,
    profileUrl: String? = null,
    timestamp: String? = null,
    type: ChatBubbleType = ChatBubbleType.SIMPLE,
    direction: ChatBubbleDirection = ChatBubbleDirection.RECEIVED,
    isEnd: Boolean = true,
    modifier: Modifier = Modifier,
    imageRequest: ImageRequest.Builder
) {
    val isSent = direction == ChatBubbleDirection.SENT

    // 버블 배경색 및 모양 설정
    val bubbleColor = if (isSent) MaterialTheme.colorScheme.tertiaryContainer
    else MaterialTheme.colorScheme.primaryContainer
    val contentColor = if (isSent) MaterialTheme.colorScheme.onTertiaryContainer
    else MaterialTheme.colorScheme.onSecondaryContainer

    val bubbleShape = if(isSent) {
        if(isEnd) {
            allSentShape
        }
        else {
            simpleShape
        }
    }
    else {
        if(type == ChatBubbleType.ALL) {
            allReceivedShape
        }
        else{
            simpleShape
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // RECEIVED 타입이면서 ALL일 때만 프로필 노출
        if (!isSent && type == ChatBubbleType.ALL) {
            AsyncImage(
                model = imageRequest
                    .data(profileUrl)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(THUMBNAIL_SIZE)
                    .clip(CircleShape)
                    .align(Alignment.Top)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        else{
            Box(
                modifier = Modifier.size(THUMBNAIL_SIZE)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isSent) Alignment.End else Alignment.Start
        ) {
            // ALL 타입일 때 이름 노출
            if (type == ChatBubbleType.ALL && userName != null && direction == ChatBubbleDirection.RECEIVED) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        bottom = 4.dp,
                        start = 4.dp,
                        end = 4.dp
                    )
                )
            }

            Row(verticalAlignment = Alignment.Bottom) {
                // 내가 보낸 메시지일 때 시간 표시 (버블 왼쪽)
                if (isSent && timestamp != null && isEnd) {
                    Text(
                        text = timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }

                Surface(
                    color = bubbleColor,
                    contentColor = contentColor,
                    shape = bubbleShape,
                    modifier = Modifier.widthIn(max = 260.dp) // 최대 너비 제한
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        softWrap = true // Multiline 지원
                    )
                }

                // 받은 메시지일 때 시간 표시 (버블 오른쪽)
                if (!isSent && timestamp != null && isEnd) {
                    Text(
                        text = timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            if(isEnd)
                Spacer(modifier.height(5.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatBubblePreview() {
    val context = LocalContext.current
    val builder = remember { ImageRequest
        .Builder(context)
        .decoderFactory(SvgDecoder.Factory())
        .crossfade(true)
        }

    SsaviceTheme {
        Column {
            // ALL 타입 - 받은 메시지
            ChatBubble(
                message = "안녕하세요! 이번에 구현하는 채팅 앱의 UI가 아주 기대되네요. 멀티라인 테스트를 위해 길게 작성해봅니다.",
                userName = "홍길동",
                profileUrl = "https://example.com/profile.jpg",
                timestamp = "오후 2:30",
                type = ChatBubbleType.ALL,
                direction = ChatBubbleDirection.RECEIVED,
                imageRequest = builder
            )

            // SIMPLE 타입 - 내가 보낸 메시지
            ChatBubble(
                message = "감사합니다! 금방 만들게요.",
                timestamp = "오후 2:31",
                type = ChatBubbleType.ALL,
                direction = ChatBubbleDirection.SENT,
                imageRequest = builder
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatBubbleMixedPreview() {
    val context = LocalContext.current
    val builder = remember { ImageRequest
        .Builder(context)
        .decoderFactory(SvgDecoder.Factory())
        .crossfade(true)
    }

    SsaviceTheme {
        Column {
            // ALL 타입 - 받은 메시지
            ChatBubble(
                message = "안녕하세요! 이번에 구현하는 채팅 앱의 UI가 아주 기대되네요. 멀티라인 테스트를 위해 길게 작성해봅니다.",
                userName = "홍길동",
                profileUrl = "https://example.com/profile.jpg",
                timestamp = "오후 2:30",
                type = ChatBubbleType.ALL,
                isEnd = false,
                direction = ChatBubbleDirection.RECEIVED,
                imageRequest = builder
            )
            ChatBubble(
                message = "그리고 이번 변경사항 커밋하시면 이메일로 알려주세요.\n수고하세용",
                userName = "홍길동",
                profileUrl = "https://example.com/profile.jpg",
                timestamp = "오후 2:30",
                type = ChatBubbleType.SIMPLE,
                direction = ChatBubbleDirection.RECEIVED,
                imageRequest = builder
            )

            // SIMPLE 타입 - 내가 보낸 메시지
            ChatBubble(
                message = "감사합니다! 금방 만들게요.",
                timestamp = "오후 2:31",
                type = ChatBubbleType.ALL,
                direction = ChatBubbleDirection.SENT,
                imageRequest = builder
            )
        }
    }
}

@Composable
fun ChatServiceBubble(
    serviceTitle: String,
    sellerName: String,
    price: String,
    thumbnailUrl: String?,
    message: String? = null,
    direction: ChatBubbleDirection = ChatBubbleDirection.RECEIVED,
    timestamp: String? = null,
    imageRequest: ImageRequest.Builder,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSent = direction == ChatBubbleDirection.SENT
    val bubbleColor = if (isSent) MaterialTheme.colorScheme.tertiaryContainer
    else MaterialTheme.colorScheme.primaryContainer
    val bubbleShape = if (isSent) allSentShape else allReceivedShape

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (isSent && timestamp != null) {
            Text(
                text = timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        Surface(
            color = bubbleColor,
            shape = bubbleShape,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                // 서비스 정보 영역
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { onDetailClick() } // 카드 전체 클릭 시 이동
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = imageRequest.data(thumbnailUrl).build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sellerName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1
                            )
                            Text(
                                text = serviceTitle,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = price,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // [변경 포인트] 우측 화살표 아이콘 버튼
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, // 더 얇고 세련된 아이콘
                            contentDescription = "이동",
                            modifier = Modifier.size(16.dp).padding(start = 4.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                if (!message.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }

        if (!isSent && timestamp != null) {
            Text(
                text = timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun ChatServiceShimmerBubble(
    direction: ChatBubbleDirection = ChatBubbleDirection.RECEIVED,
    timestamp: String? = null,
    isEnd: Boolean = true,
    modifier: Modifier = Modifier
) {
    val brush = shimmerBrush()
    val isSent = direction == ChatBubbleDirection.SENT

    val bubbleColor = if (isSent) MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
    else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)

    val bubbleShape = if (isSent) {
        if (isEnd) allSentShape else simpleShape
    } else {
        if (isEnd) allReceivedShape else simpleShape
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isSent) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // 보낸 메시지 시간 쉬머 상태 노출 (왼쪽)
        if (isSent && timestamp != null && isEnd) {
            Text(
                text = timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        Surface(
            color = bubbleColor,
            shape = bubbleShape,
            modifier = Modifier.width(260.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        // 썸네일 박스
                        Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(brush))
                        Spacer(modifier = Modifier.width(10.dp))

                        // 텍스트 영역
                        Column(modifier = Modifier.weight(1f)) {
                            Box(modifier = Modifier.width(40.dp).height(10.dp).background(brush))
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(modifier = Modifier.fillMaxWidth(0.8f).height(14.dp).background(brush))
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(modifier = Modifier.width(60.dp).height(12.dp).background(brush))
                        }

                        // 화살표 아이콘 위치
                        Box(modifier = Modifier.size(16.dp).background(brush))
                    }
                }
            }
        }

        // 받은 메시지 시간 쉬머 상태 노출 (오른쪽)
        if (!isSent && timestamp != null && isEnd) {
            Text(
                text = timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}
