package com.ssavice.user_chatting

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.ssavice.model.chat.ChattingRoomMetadata
import com.ssavice.user_chatting.ui.ChattingRoomItem

@Composable
fun UserChattingRoute(
    modifier: Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
    onRoomClick: (id: String) -> Unit = {},
) {
    val state by viewModel.chattingRoomState.collectAsStateWithLifecycle()

    ChatList(
        modifier = modifier,
        rooms = state,
        onRoomClick,
        onRefresh = viewModel::onRefresh,
    )
}

@Composable
fun ChatList(
    modifier: Modifier,
    rooms: List<ChattingRoomItem>,
    onRoomClick: (id: String) -> Unit = {},
    onRefresh: () -> Unit,
) {
    val context = LocalContext.current
    val imageRequestBuilder =
        remember {
            ImageRequest
                .Builder(context)
                .crossfade(true)
                .placeholder(android.R.drawable.ic_menu_info_details)
        }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer =
            androidx.lifecycle.LifecycleEventObserver { _, event ->
                if (event == androidx.lifecycle.Lifecycle.Event.ON_START) {
                    onRefresh()
                }
            }

        lifecycleOwner.lifecycle.addObserver(observer)

        // Composable이 파괴될 때 옵저버를 제거합니다.
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
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
            verticalArrangement = if (rooms.isEmpty()) Arrangement.Center else Arrangement.Top,
        ) {
            items(
                count = rooms.size,
                key = { index -> rooms[index].roomId }, // name 대신 고유한 roomId를 key로 권장
            ) { index ->
                RoomItem(
                    modifier = Modifier.fillMaxWidth(),
                    room = rooms[index],
                    onClick = onRoomClick,
                    imageRequestBuilder = imageRequestBuilder,
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
    imageRequestBuilder: ImageRequest.Builder,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable {
                    onClick(room.roomId)
                },
        verticalArrangement = spacedBy(5.dp),
    ) {
        ChattingRoomItem(
            title = room.name,
            lastMessage = room.lastMessage ?: "",
            thumbnailUrl = null,
            updatedAt = room.lastUpdate.dateToSimpleString(),
            unreadCount = room.unreadCount,
            onClick = { onClick(room.roomId) },
            requestBuilder = imageRequestBuilder,
        )
    }
}
