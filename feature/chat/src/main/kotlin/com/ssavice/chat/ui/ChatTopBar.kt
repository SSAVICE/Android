package com.ssavice.chat.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.chat.ChattingViewModel
import com.ssavice.designsystem.component.SsavicePopUpTopBar
import com.ssavice.designsystem.component.SsaviceTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    defaultTitle: String,
    viewModel: ChattingViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    contentPadding: PaddingValues = TopAppBarDefaults.ContentPadding,
    onDrawerOpenClick: () -> Unit = {}
) {
    val state = viewModel.roomUiState.collectAsStateWithLifecycle()
    val title = if (state.value.roomName != "") state.value.roomName else defaultTitle

    TopAppBar(
        title = {
            ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                Text(text = title, fontWeight = FontWeight.SemiBold)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        actions = {
            IconButton(onClick = onDrawerOpenClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Drawer",
                )
            }
        },
        contentPadding = contentPadding,
    )
}
