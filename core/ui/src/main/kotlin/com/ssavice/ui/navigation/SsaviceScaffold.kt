package com.ssavice.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import com.ssavice.designsystem.component.LocalSnackbarHostState

@Composable
fun SsaviceScaffold(
    bottomBar: @Composable () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val globalSnackBarHostState = LocalSnackbarHostState.current
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        content = content,
        snackbarHost = { SnackbarHost(hostState = globalSnackBarHostState) }
    )
}
