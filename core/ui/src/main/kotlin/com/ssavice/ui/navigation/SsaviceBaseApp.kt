package com.ssavice.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController

@Composable
fun SsaviceBaseApp(
    navController: NavHostController,
    defaultBottomBar: @Composable (navController: NavHostController, currentRoute: String?) -> Unit,
    content: @Composable (PaddingValues, (ScaffoldConfig) -> Unit) -> Unit,
) {
    var scaffoldConfig by remember<MutableState<ScaffoldConfig>> {
        mutableStateOf(ScaffoldConfig.Default) // 초기값
    }
    val currentDestination by remember {
        derivedStateOf { navController.currentBackStackEntry?.destination }
    }

    val onScaffoldConfigResolved: (ScaffoldConfig) -> Unit = { config ->
        scaffoldConfig = config
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val bottomBar: @Composable () -> Unit
    val topBar: @Composable () -> Unit

    when (val config = scaffoldConfig) {
        is ScaffoldConfig.Default -> {
            bottomBar = {
                defaultBottomBar(navController, currentDestination?.route)
            }
            topBar = {}
        }

        is ScaffoldConfig.None -> {
            bottomBar = {}
            topBar = {}
        }

        is ScaffoldConfig.CustomTopWithDefaultBottom -> {
            bottomBar = {
                defaultBottomBar(navController, currentDestination?.route)
            }
            topBar = config.topBar ?: {}
        }

        is ScaffoldConfig.Custom -> {
            bottomBar = config.bottomBar ?: {}
            topBar = config.topBar ?: {}
        }

        is ScaffoldConfig.TitleAndDefaultBottom -> {
            bottomBar = {
                defaultBottomBar(navController, currentDestination?.route)
            }
            topBar = {
                SsaviceTitle(
                    title = config.title,
                    onBackButtonClick =
                        if (config.onBackButtonClick != null) {
                            {
                                keyboardController?.hide()
                                config.onBackButtonClick()
                            }
                        } else {
                            null
                        },
                    action = {},
                )
            }
        }

        is ScaffoldConfig.TitleWithCustomBottom -> {
            bottomBar = config.bottomBar ?: {}
            topBar = {
                SsaviceTitle(
                    title = config.title,
                    onBackButtonClick =
                        if (config.onBackButtonClick != null) {
                            {
                                keyboardController?.hide()
                                config.onBackButtonClick()
                            }
                        } else {
                            null
                        },
                    action = {},
                )
            }
        }
    }

    SsaviceScaffold(
        bottomBar = bottomBar,
        topBar = topBar,
    ) { innerPadding ->
        content(innerPadding, onScaffoldConfigResolved)
    }
}
