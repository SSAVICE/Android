package com.ssavice.chat_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
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
import coil.request.ImageRequest
import com.ssavice.chat_list.ui.ChattingRoomItem
import com.ssavice.ui.common.Constant

@Composable
fun UserChattingRoute(
    modifier: Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
    onRoomClick: (id: String) -> Unit = {},
) {
    val state by viewModel.chattingRoomState.collectAsStateWithLifecycle()

    var isTransitionFinished by remember { mutableStateOf(false) }

    // [추가] 화면 진입 후 일정 시간(애니메이션 시간) 동안 대기
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(Constant.ANIMATION_DELAY)
        isTransitionFinished = true
    }

    ChatList(
        modifier = modifier,
        rooms = if (isTransitionFinished) state else emptyList(),
        onRoomClick = onRoomClick,
        onRefresh = viewModel::onRefresh,
        isTransitionFinished = isTransitionFinished
    )
}

@Composable
fun ChatList(
    modifier: Modifier,
    rooms: List<ChattingRoomItem>,
    onRoomClick: (id: String) -> Unit = {},
    onRefresh: () -> Unit,
    isTransitionFinished: Boolean
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, isTransitionFinished) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_START && isTransitionFinished) {
                onRefresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (!isTransitionFinished) {
        // 애니메이션 중에는 아무것도 그리지 않거나 아주 가벼운 로딩 인디케이터만 표시
        return
    }

    if (rooms.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(), // 부모 크기만큼 차지
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
