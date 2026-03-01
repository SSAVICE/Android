package com.ssavice.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ssavice.model.chat.Chat

@SuppressLint("FrequentlyChangingValue")
@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
) {
    val chatMessages: LazyPagingItems<Chat> = viewModel.pagingState.collectAsLazyPagingItems()
    val listState = rememberLazyListState()
    val roomState by viewModel.roomInfoState.collectAsStateWithLifecycle()

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

    LaunchedEffect(roomState.loadState) {
        if (roomState.loadState == ChattingRoomLoadState.Initial) {
            viewModel.loadChattingRoomInfo()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row {
            Text(listState.firstVisibleItemIndex.toString())
        }
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
                if (message != null) {
                    ChatMessageItem(message, roomState)
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    chat: Chat,
    roomInfo: ChattingRoomUiState,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
    ) {
        val userName: String = roomInfo.userInfo[chat.senderId]?.name ?: ""
        val userThumbnail: String = roomInfo.userInfo[chat.senderId]?.thumbnail ?: ""
        Text(text = userName, fontWeight = FontWeight.Bold)
        Text(text = chat.content)
    }
}
