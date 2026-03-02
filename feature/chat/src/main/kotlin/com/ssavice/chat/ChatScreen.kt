package com.ssavice.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ssavice.designsystem.component.ChatBubble
import com.ssavice.designsystem.component.ChatBubbleDirection
import com.ssavice.designsystem.component.ChatBubbleType
import com.ssavice.designsystem.component.ChatServiceBubble
import com.ssavice.designsystem.component.ChatServiceShimmerBubble
import com.ssavice.designsystem.theme.shimmerBrush
import com.ssavice.model.DateTime

@SuppressLint("FrequentlyChangingValue")
@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val imageRequestBuilder =
        remember {
            ImageRequest
                .Builder(context)
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
        }

    Box(modifier = modifier.fillMaxSize()) {
        if (uiState.sendingService) {
            viewModel.requestServiceInfo(uiState.serviceIdToSend)
            SendServicePreviewItem(
                serviceInfo = uiState.serviceInfo[uiState.serviceIdToSend],
                imageRequestBuilder = imageRequestBuilder,
                modifier =
                    Modifier
                        .padding(10.dp)
                        .align(
                            Alignment.BottomCenter,
                        ),
            )
        }

        if (uiState.chattingRoomState != ChattingRoomState.Pending) {
            ChattingScreen(
                modifier = Modifier,
                viewModel = viewModel,
                uiState = uiState,
                imageRequestBuilder = imageRequestBuilder,
            )
        }
    }
}

@Composable
fun ChattingScreen(
    modifier: Modifier = Modifier,
    viewModel: ChattingViewModel,
    uiState: ChattingRoomUiState,
    imageRequestBuilder: ImageRequest.Builder,
) {
    val chatMessages: (LazyPagingItems<ChatMessage>) =
        viewModel.pagingState.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    val top by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex <= 2
        }
    }
    LaunchedEffect(chatMessages.itemCount) {
        if (chatMessages.itemCount > 0) {
            if (top) {
                listState.animateScrollToItem(0)
            }
            chatMessages.peek(0)?.let {
                viewModel.updateLastRead(it.messageId.toInt())
            }
        }
    }

    LaunchedEffect(uiState.roomInfoLoadState) {
        if (uiState.roomInfoLoadState == ChattingRoomInfoLoadState.Initial) {
            viewModel.loadChattingRoomInfo()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState,
            reverseLayout = true,
        ) {
            // 2. items 함수로 페이징 데이터 렌더링
            items(
                count = chatMessages.itemCount,
                key = chatMessages.itemKey { it.messageId },
            ) { index ->
                val message = chatMessages[index]
                val before =
                    if (index == chatMessages.itemCount - 1) null else chatMessages.peek(index + 1)

                if (message == null) return@items

                val full =
                    if (before == null) {
                        true
                    } else {
                        (message.userId != before.userId) ||
                            message.time != before.time
                    }

                when (val t = message) {
                    is ChatMessage.ServiceMessage -> {
                        val service = uiState.serviceInfo[t.serviceId]

                        if (service == null) {
                            ChatServiceShimmerBubble(
                                direction = if (t.you) ChatBubbleDirection.SENT else ChatBubbleDirection.RECEIVED,
                                timestamp = t.time.timeToSimpleString(),
                                isEnd = true,
                            )
                        } else {
                            ChatServiceBubble(
                                serviceTitle = service.name,
                                sellerName = service.seller,
                                price = "₩%,d".format(service.discountPrice),
                                thumbnailUrl = service.thumbnail,
                                direction = if (t.you) ChatBubbleDirection.SENT else ChatBubbleDirection.RECEIVED,
                                timestamp = t.time.timeToSimpleString(),
                                imageRequest = imageRequestBuilder,
                                onDetailClick = {},
                            )
                        }
                    }

                    is ChatMessage.TextMessage -> {
                        ChatMessageItem(
                            text = t.text,
                            time = t.time,
                            simple = !full,
                            userName = message.userId.toString(),
                            profile = "",
                            isYou = t.you,
                            end = t.first,
                            imageRequestBuilder = imageRequestBuilder,
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun SendServicePreviewItem(
    serviceInfo: ServiceInfo?,
    imageRequestBuilder: ImageRequest.Builder,
    modifier: Modifier = Modifier,
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
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("다음 상품에 대해 물어보세요!")
            Spacer(Modifier.height(5.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 1. 상품 썸네일 영역
                if (isDataLoading) {
                    Box(
                        modifier =
                            Modifier
                                .size(48.dp)
                                .clip(
                                    androidx.compose.foundation.shape
                                        .RoundedCornerShape(8.dp),
                                ).background(brush),
                    )
                } else {
                    AsyncImage(
                        model = imageRequestBuilder.data(serviceInfo.thumbnail).build(),
                        contentDescription = null,
                        modifier =
                            Modifier
                                .size(48.dp)
                                .clip(
                                    androidx.compose.foundation.shape
                                        .RoundedCornerShape(8.dp),
                                ),
                        contentScale = ContentScale.Crop,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 2. 정보 영역 (판매자, 제목, 가격)
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    if (isDataLoading) {
                        // Shimmering Placeholders
                        Box(
                            modifier =
                                Modifier
                                    .width(60.dp)
                                    .height(12.dp)
                                    .background(brush),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(16.dp)
                                    .background(brush),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier =
                                Modifier
                                    .width(80.dp)
                                    .height(14.dp)
                                    .background(brush),
                        )
                    } else {
                        serviceInfo?.let { info ->
                            Text(
                                text = info.seller,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = info.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = "%,d원".format(info.discountPrice),
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

@Composable
fun ChatMessageItem(
    text: String,
    time: DateTime,
    simple: Boolean,
    userName: String,
    profile: String,
    end: Boolean,
    isYou: Boolean,
    imageRequestBuilder: ImageRequest.Builder,
) {
    ChatBubble(
        message = text,
        userName = userName,
        profileUrl = profile,
        timestamp = time.timeToSimpleString(),
        type = if (simple) ChatBubbleType.SIMPLE else ChatBubbleType.ALL,
        direction = if (!isYou) ChatBubbleDirection.RECEIVED else ChatBubbleDirection.SENT,
        isEnd = end,
        imageRequest = imageRequestBuilder,
    )
}
