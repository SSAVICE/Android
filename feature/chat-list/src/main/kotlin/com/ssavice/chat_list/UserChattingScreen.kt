package com.ssavice.chat_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.request.ImageRequest
import com.ssavice.chat_list.ui.ChatRoomSkeletonItem
import com.ssavice.chat_list.ui.ChattingRoomItem
import com.ssavice.designsystem.theme.shimmerBrush
import com.ssavice.ui.common.Constant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserChattingRoute(
    modifier: Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
    onRoomClick: (id: String) -> Unit = {},
) {
    val state by viewModel.chattingRoomState.collectAsStateWithLifecycle()
    val initialLoadState by viewModel.initialLoad.collectAsStateWithLifecycle()

    // [추가] 화면 진입 후 일정 시간(애니메이션 시간) 동안 대기
    LaunchedEffect(Unit) {
        if (initialLoadState) {
            delay(Constant.ANIMATION_DELAY)
            viewModel.onTransitionFinished()
        }
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, initialLoadState) {
        val observer =
            androidx.lifecycle.LifecycleEventObserver { _, event ->
                if (event == androidx.lifecycle.Lifecycle.Event.ON_START && !initialLoadState) {
                    lifecycleOwner.lifecycleScope.launch {
                        delay(Constant.ANIMATION_DELAY)
                        viewModel.onRefresh()
                    }
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    ChatList(
        modifier = modifier,
        rooms = if (!initialLoadState) state else emptyList(),
        onRoomClick = onRoomClick,
        isTransitionFinished = !initialLoadState,
    )
}

@Composable
fun ChatList(
    modifier: Modifier,
    rooms: List<ChattingRoomItem>,
    onRoomClick: (id: String) -> Unit = {},
    isTransitionFinished: Boolean,
) {
    if (!isTransitionFinished) {
        val brush = shimmerBrush()
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(5) {
                // 로딩 중 10개의 가짜 아이템 표시
                ChatRoomSkeletonItem(brush = brush)
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                )
            }
        }
        return
    }

    if (rooms.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(), // 부모 크기만큼 차지
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "참여 중인 채팅방이 없습니다.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(), // 중앙 배치를 위해 fillMaxSize 추가
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            items(
                count = rooms.size,
                key = { index -> rooms[index].roomId }, // name 대신 고유한 roomId를 key로 권장
            ) { index ->
                RoomItem(
                    modifier = Modifier.fillMaxWidth(),
                    room = rooms[index],
                    onClick = onRoomClick,
                )
                if (index < rooms.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun RoomItem(
    modifier: Modifier,
    room: ChattingRoomItem,
    onClick: (id: String) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalArrangement = spacedBy(5.dp),
    ) {
        ChattingRoomItem(
            title = room.name,
            lastMessage = room.lastMessage ?: "",
            thumbnailUrl = null,
            updatedAt = room.lastUpdateString,
            unreadCount = room.unreadCount,
            onClick = { onClick(room.roomId) },
        )
    }
}
