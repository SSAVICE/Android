package com.ssavice_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.seller_main.navigation.MainRoute
import com.ssavice.seller_register.navigation.RegisterRoute
import com.ssavice.ui.navigation.SsaviceBaseApp
import com.ssavice_seller.navigation.SsaviceNavHost
import com.ssavice_seller.ui.SsaviceBottomBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
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
            startDestination = RegisterRoute,
            onScaffoldConfigResolved = config,
        )
    }
}
