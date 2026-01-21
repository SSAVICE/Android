package com.ssavice_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.network.AuthEvent
import com.ssavice.network.AuthEventManager
import com.ssavice.seller_main.navigation.MainRoute
import com.ssavice.seller_main.navigation.navigateToLogin
import com.ssavice.seller_register.navigation.RegisterRoute
import com.ssavice.seller_register.navigation.navigateToRegister
import com.ssavice.ui.common.collectAsEffect
import com.ssavice.ui.navigation.SsaviceBaseApp
import com.ssavice_seller.navigation.SsaviceNavHost
import com.ssavice_seller.ui.SsaviceBottomBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authEventManager: AuthEventManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            authEventManager.event.collectAsEffect {
                when(it) {
                    is AuthEvent.Unauthorized -> {
                        navController.navigateToLogin()
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
    }
}

@Composable
fun SsaviceSellerApp(navController: NavHostController) {
    SsaviceBaseApp(
        navController = navController,
        defaultBottomBar = { navController ->
            SsaviceBottomBar(navController)
        },
    ) { innerPadding, config ->
        SsaviceNavHost(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            navController = navController,
            startDestination = MainRoute,
            onScaffoldConfigResolved = config,
        )
    }
}
