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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.model.chat.ChattingRoomMetadata

@Composable
fun UserChattingRoute(
    modifier: Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
    onRoomClick: (id: Long) -> Unit = {},
) {
    val state by viewModel.chattingRoomState.collectAsStateWithLifecycle()

    ChatList(modifier, state, onRoomClick)
}

@Composable
fun ChatList(
    modifier: Modifier,
    rooms: List<ChattingRoomMetadata>,
    onRoomClick: (id: Long) -> Unit = {},
) {
    LazyColumn(modifier = modifier) {
        items(rooms.size, key = { index -> rooms[index].name }) { index ->
            RoomItem(
                modifier = Modifier.fillMaxWidth(),
                room = rooms[index],
                onClick = onRoomClick,
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
    room: ChattingRoomMetadata,
    onClick: (id: Long) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clickable {
                    onClick(room.roomId)
                },
        verticalArrangement = spacedBy(5.dp),
    ) {
        Text(room.name, style = MaterialTheme.typography.titleMedium)
        Text(room.lastMessage)
        Text(("읽지 않은 메시지: ${room.unreadCount}"), style = MaterialTheme.typography.labelMedium)
    }
}
