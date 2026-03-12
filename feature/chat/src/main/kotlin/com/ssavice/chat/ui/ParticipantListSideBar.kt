package com.ssavice.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ssavice.chat.ChattingRoomState
import com.ssavice.chat.ChattingViewModel
import com.ssavice.model.chat.ChattingUserInfo

@Composable
fun ParticipantListSideBar(
    modifier: Modifier = Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
) {
    val roomUiState by viewModel.roomUiState.collectAsStateWithLifecycle()
    val chattingState by viewModel.chattingUiState.collectAsStateWithLifecycle()

    ModalDrawerSheet(
        modifier = modifier.width(280.dp), // 사이드바 너비 조절
        drawerContainerColor = MaterialTheme.colorScheme.surface,
    ) {
        if (roomUiState.chattingRoomState == ChattingRoomState.Ready) {
            ParticipantListSideBar(
                participants = roomUiState.participantIds,
                participantInfo = chattingState.userInfo,
                yourId = roomUiState.yourId,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ParticipantListSideBar(
    modifier: Modifier = Modifier,
    participants: List<Long>,
    participantInfo: Map<Long, ChattingUserInfo>,
    yourId: Long,
) {
    // 1. 내 정보를 최상단으로 올리고 이름을 "당신"으로 변경하는 로직
    val sortedParticipants =
        remember(participants, yourId) {
            val mine =
                participants.find { it == yourId }?.let {
                    ChattingUserInfo(name = "당신", thumbnail = (participantInfo[it]?.thumbnail) ?: "", id = it)
                }
            val others =
                participants.filter { it != yourId }.map {
                    participantInfo.getOrElse(it) {
                        ChattingUserInfo(name = "알 수 없음", thumbnail = "", id = it)
                    }
                }
            listOfNotNull(mine) + others
        }
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Text(
                text = "대화 상대",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(sortedParticipants, key = { it.id }) { participant ->
                    ParticipantItem(participant)
                }
            }
        }
    }
}

@Composable
private fun ParticipantItem(participant: ChattingUserInfo) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
    ) {
        // 유저 썸네일
        AsyncImage(
            model = participant.thumbnail,
            contentDescription = null,
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 유저 이름
        Text(
            text = participant.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
