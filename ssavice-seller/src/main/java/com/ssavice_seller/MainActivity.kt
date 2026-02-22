package com.ssavice_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kakao.vectormap.KakaoMapSdk
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.login.navigation.navigateToLogin
import com.ssavice.network.AuthEvent
import com.ssavice.network.AuthEventManager
import com.ssavice.seller_main.navigation.MainRoute
import com.ssavice.seller_register.navigation.navigateToRegister
import com.ssavice.ui.common.collectAsEffect
import com.ssavice_seller.navigation.SsaviceNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authEventManager: AuthEventManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            authEventManager.event.collectAsEffect {
                when (it) {
                    is AuthEvent.Unauthorized -> {
                        navController.navigateToLogin(isUser = false)
                    }

                    is AuthEvent.Forbidden -> {
                        navController.navigateToRegister()
                    }

                    else -> { }
                }
            }

            SsaviceTheme {
                SsaviceSellerApp(navController)
            }
        }
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY_SELLER)
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
