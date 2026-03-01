package com.ssavice.ssavice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import com.ssavice.chat.WebSocketEventHandler
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.login.navigation.navigateToLogin
import com.ssavice.network.AuthEvent
import com.ssavice.network.AuthEventManager
import com.ssavice.network.websocket.ChatWebSocketManager
import com.ssavice.ssavice.navigation.SsaviceNavHost
import com.ssavice.ui.common.collectAsEffect
import com.ssavice.user_main.navigation.MainRoute
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authEventManager: AuthEventManager
    @Inject lateinit var webSocketManager: ChatWebSocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            authEventManager.event.collectAsEffect {
                when (it) {
                    is AuthEvent.Unauthorized -> {
                        navController.navigateToLogin(isUser = true)
                    }

                    else -> { }
                }
            }
            SsaviceTheme {
                SsaviceApp(navController)
            }
        }
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }

    override fun onStart() {
        webSocketManager.connect()
        super.onStart()
    }

    override fun onStop() {
        webSocketManager.close()
        super.onStop()
    }
}

@Composable
fun SsaviceApp(navController: NavHostController) {
    SsaviceNavHost(
        modifier =
            Modifier
                .fillMaxSize(),
        navController = navController,
        startDestination = MainRoute,
    )
}
