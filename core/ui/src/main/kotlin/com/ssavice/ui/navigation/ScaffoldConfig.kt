package com.ssavice.ui.navigation

import androidx.compose.runtime.Composable

// Scaffold 구성을 위한 Sealed Class
sealed interface ScaffoldConfig {
    // 기본 BottomBar를 사용하는 경우
    data object Default : ScaffoldConfig

    // BottomBar/TopBar가 없는 경우
    data object None : ScaffoldConfig

    data class TitleAndDefaultBottom(
        val title: String,
        val onBackButtonClick: (() -> Unit)? = null,
    ) : ScaffoldConfig

    data class TitleWithCustomBottom(
        val title: String,
        val onBackButtonClick: (() -> Unit)? = null,
        val bottomBar: (@Composable () -> Unit)? = null
    ) : ScaffoldConfig

    data class CustomTopWithDefaultBottom(
        val topBar: (@Composable () -> Unit)? = null,
    ) : ScaffoldConfig

    // 완전히 커스텀 UI를 사용하는 경우
    data class Custom(
        val topBar: (@Composable () -> Unit)? = null,
        val bottomBar: (@Composable () -> Unit)? = null
    ) : ScaffoldConfig
}
