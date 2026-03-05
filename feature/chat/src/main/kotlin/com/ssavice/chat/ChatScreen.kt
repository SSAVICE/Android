package com.ssavice.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.ssavice.chat.ui.ChatDivider
import com.ssavice.chat.ui.ChatMessageItem
import com.ssavice.chat.ui.SendServicePreviewItem
import com.ssavice.designsystem.component.ChatBubbleDirection
import com.ssavice.designsystem.component.ChatServiceBubble
import com.ssavice.designsystem.component.ChatServiceShimmerBubble

@SuppressLint("FrequentlyChangingValue")
@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
) {
    val roomUiState by viewModel.roomUiState.collectAsStateWithLifecycle()
    val chattingState by viewModel.chattingUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val imageRequestBuilder =
        remember {
            ImageRequest
                .Builder(context)
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
        }

    LaunchedEffect(
        roomUiState,
    ) {
        when (roomUiState.chattingRoomState) {
            ChattingRoomState.Initial -> {
                viewModel.getYourId()
            }

            else -> {}
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (roomUiState.sendingService) {
            SendServicePreviewItem(
                serviceInfo = chattingState.serviceInfo[roomUiState.serviceIdToSend],
                imageRequestBuilder = imageRequestBuilder,
                modifier =
                    Modifier
                        .padding(10.dp)
                        .align(
                            Alignment.BottomCenter,
                        ),
            )
        }

        if (roomUiState.chattingRoomState == ChattingRoomState.Ready) {
            ChattingScreen(
                modifier = Modifier,
                viewModel = viewModel,
                roomUiState = roomUiState,
                chattingUiState = chattingState,
                imageRequestBuilder = imageRequestBuilder,
            )
        }
    }
}

@Composable
fun ChattingScreen(
    modifier: Modifier = Modifier,
    viewModel: ChattingViewModel,
    chattingUiState: ChattingDataUiState,
    roomUiState: RoomUiState,
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

    LaunchedEffect(roomUiState.roomInfoLoadState) {
        if (roomUiState.roomInfoLoadState == ChattingRoomInfoLoadState.Initial) {
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
                Column {
                    val message = chatMessages[index]
                    val before =
                        if (index == chatMessages.itemCount - 1) null else chatMessages.peek(index + 1)
                    val last = (index == chatMessages.itemCount - 1)

                    if (message == null) return@items

                    val full =
                        if (before == null) {
                            true
                        } else {
                            (message.userId != before.userId) ||
                                message.time != before.time
                        }

                    val insertDayDivider =
                        if (before == null) {
                            false
                        } else {
                            (
                                before.time.day != message.time.day ||
                                    before.time.month != message.time.month ||
                                    before.time.year != message.time.year
                            )
                        }

                    if (last || insertDayDivider) {
                        ChatDivider(
                            message = message.time.absoluteDateSimpleString(),
                        )
                    }
                    when (val t = message) {
                        is ChatMessage.ServiceMessage -> {
                            val service = chattingUiState.serviceInfo[t.serviceId]

                            if (service == null) {
                                ChatServiceShimmerBubble(
                                    direction = if (t.you) ChatBubbleDirection.SENT else ChatBubbleDirection.RECEIVED,
                                    timestamp = t.time.timeToSimpleString(),
                                    isEnd = true,
                                )
                            } else {
                                ChatServiceBubble(
                                    serviceTitle = service.serviceName,
                                    sellerName = service.serviceSeller,
                                    price = "₩%,d".format(service.servicePrice),
                                    thumbnailUrl = service.serviceThumbnail,
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
}
