package com.ssavice.service_detail.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.service_detail.InfoState
import com.ssavice.service_detail.ServiceDetailViewModel

@Composable
fun ServiceDetailBottomBar(
    modifier: Modifier = Modifier,
    viewModel: ServiceDetailViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedButton(
            onClick = viewModel::onChatButtonClick,
            modifier = Modifier.weight(1f),
            enabled = state.sellerInfoState is InfoState.Done,
        ) {
            Text("채팅하기")
        }
        Button(
            onClick = viewModel::onParticipateButtonClick,
            modifier = Modifier.weight(1f),
            enabled =
                state.serviceInfoState is InfoState.Done && state.service?.applied == false &&
                    (state.applyInfoState is InfoState.Waiting || state.applyInfoState is InfoState.Error),
        ) {
            Text(if (state.service?.applied ?: false) "참여 중" else "참여하기")
        }
    }
}
