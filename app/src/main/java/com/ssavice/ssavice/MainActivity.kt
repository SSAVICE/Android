package com.ssavice.ssavice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.ssavice.navigation.ScaffoldConfig
import com.ssavice.ssavice.navigation.SsaviceNavHost
import com.ssavice.ssavice.ui.SsaviceBottomBar
import com.ssavice.ssavice.ui.SsaviceScaffold
import com.ssavice.ssavice.ui.SsaviceTitle
import com.ssavice.user_main.navigation.MainRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SsaviceTheme {
                SsaviceApp(navController)
            }
        }
    }
}

@Composable
fun SsaviceApp(
    navController: NavHostController
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

    // 4. State에 따라 Scaffold의 구성을 동적으로 결정
    val bottomBar: @Composable () -> Unit
    val topBar: @Composable () -> Unit

    when (val config = scaffoldConfig) {
        is ScaffoldConfig.Default -> {
            bottomBar = {
                SsaviceBottomBar(
                    navController = navController,
                    currentRoute = currentDestination?.route
                )
            }
            topBar = {}
        }

        is ScaffoldConfig.None -> {
            bottomBar = {}
            topBar = {}
        }

        is ScaffoldConfig.CustomTopWithDefaultBottom -> {
            bottomBar = {
                SsaviceBottomBar(
                    navController = navController,
                    currentRoute = currentDestination?.route
                )
            }
            topBar = config.topBar ?: {}
        }

        is ScaffoldConfig.Custom -> {
            bottomBar = config.bottomBar ?: {}
            topBar = config.topBar ?: {}
        }

        is ScaffoldConfig.TitleAndDefaultBottom -> {
            bottomBar = {
                SsaviceBottomBar(
                    navController = navController,
                    currentRoute = currentDestination?.route
                )
            }
            topBar = {
                SsaviceTitle(
                    title = config.title,
                    onBackButtonClick = if(config.onBackButtonClick != null){
                        {
                            keyboardController?.hide()
                            config.onBackButtonClick()
                        }
                    } else null,
                    action = {}
                )
            }
        }

        is ScaffoldConfig.TitleWithCustomBottom -> {
            bottomBar = config.bottomBar ?: {}
            topBar = {
                SsaviceTitle(
                    title = config.title,
                    onBackButtonClick = if(config.onBackButtonClick != null){
                        {
                            keyboardController?.hide()
                            config.onBackButtonClick()
                        }
                    } else null,
                    action = {}
                )
            }
        }
    }

    SsaviceScaffold(
        bottomBar = bottomBar,
        topBar = topBar
    ) { innerPadding ->
        SsaviceNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
            startDestination = MainRoute,
            onScaffoldConfigResolved = onScaffoldConfigResolved
        )
    }

}
