package com.ssavice_seller

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import com.ssavice.designsystem.component.LocalSnackbarHostState
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.login.navigation.navigateToLogin
import com.ssavice.network.NetworkEvent
import com.ssavice.network.NetworkEventManager
import com.ssavice.network.websocket.ChatWebSocketManager
import com.ssavice.seller_main.navigation.MainRoute
import com.ssavice.seller_register.navigation.navigateToRegister
import com.ssavice.ui.common.collectAsEffect
import com.ssavice_seller.navigation.SsaviceNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var networkEventManager: NetworkEventManager

    @Inject lateinit var webSocketManager: ChatWebSocketManager

    private fun sendSnackBar(
        text: String,
        snackbarHostState: SnackbarHostState,
        duration: SnackbarDuration = SnackbarDuration.Short,
        lastCollectedUiEventTime: MutableLongState,
        hasDismissButton: Boolean = false,
    ) {
        if (System.currentTimeMillis() - lastCollectedUiEventTime.longValue < 3000L) return
        lifecycleScope.launch {
            snackbarHostState.showSnackbar(
                message = text,
                withDismissAction = hasDismissButton,
                duration = duration,
            )
        }
        lastCollectedUiEventTime.longValue = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            var lastCollectedUiEventTime = remember { mutableLongStateOf(0L) }

            networkEventManager.event.collectAsEffect {
                when (it) {
                    is NetworkEvent.Unauthorized -> {
                        navController.navigateToLogin(isUser = false)
                    }

                    is NetworkEvent.Forbidden -> {
                        navController.navigateToRegister()
                    }

                    is NetworkEvent.NetworkUnavailable -> {
                        Log.e("KSC", "onCreate: NetworkUnavailable")
                        sendSnackBar(
                            text = "네트워크 연결이 끊겼습니다.",
                            snackbarHostState = snackbarHostState,
                            lastCollectedUiEventTime = lastCollectedUiEventTime,
                            hasDismissButton = true,
                        )
                    }

                    is NetworkEvent.ChatServerUnavailable -> {
                        Log.e("KSC", "onCreate: ChatServerUnavailable")
                        sendSnackBar(
                            text = "채팅 서버와의 연결이 끊겼습니다.",
                            snackbarHostState = snackbarHostState,
                            lastCollectedUiEventTime = lastCollectedUiEventTime,
                            hasDismissButton = true,
                        )
                    }

                    else -> { }
                }
            }

            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                SsaviceTheme {
                    SsaviceSellerApp(navController)
                }
            }
        }
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY_SELLER)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY_SELLER)
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
fun SsaviceSellerApp(navController: NavHostController) {
    SsaviceNavHost(
        modifier =
            Modifier
                .fillMaxSize(),
        navController = navController,
        startDestination = MainRoute,
    )
}
