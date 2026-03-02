package com.ssavice.user_chatting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

    ChatList(modifier, state, onRoomClick)
}

@Composable
fun ChatList(
    modifier: Modifier,
    rooms: List<ChattingRoomItem>,
    onRoomClick: (id: String) -> Unit = {},
) {
    val context = LocalContext.current
    val imageRequestBuilder = remember {
        ImageRequest.Builder(context)
            .crossfade(true)
            .placeholder(android.R.drawable.ic_menu_info_details)
    }

    LazyColumn(modifier = modifier) {
        items(rooms.size, key = { index -> rooms[index].name }) { index ->
            RoomItem(
                modifier = Modifier.fillMaxWidth(),
                room = rooms[index],
                onClick = onRoomClick,
                imageRequestBuilder = imageRequestBuilder
            )
            if (index < rooms.size - 1) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
            }
        }
    }
}

@Composable
fun RoomItem(
    modifier: Modifier,
    room: ChattingRoomItem,
    onClick: (id: String) -> Unit = {},
    imageRequestBuilder: ImageRequest.Builder
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
            lastMessage = room.lastMessage?:"",
            thumbnailUrl = null,
            updatedAt = room.lastUpdate.dateToSimpleString(),
            unreadCount = room.unreadCount,
            onClick = {onClick(room.roomId)},
            requestBuilder = imageRequestBuilder
        )
    }
}
