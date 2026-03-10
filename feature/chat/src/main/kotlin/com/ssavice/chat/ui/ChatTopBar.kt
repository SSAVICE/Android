package com.ssavice.chat.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.chat.ChattingViewModel
import com.ssavice.designsystem.component.SsavicePopUpTopBar

@Composable
fun ChatTopBar(
    defaultTitle: String,
    viewModel: ChattingViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
) {
    val state = viewModel.roomUiState.collectAsStateWithLifecycle()
    val title = if(state.value.roomName != "") state.value.roomName else  defaultTitle

    SsavicePopUpTopBar(
        title = title,
        onBackClicked = onBack,
    )
}
