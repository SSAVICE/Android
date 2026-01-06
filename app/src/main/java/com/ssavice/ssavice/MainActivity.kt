package com.ssavice.ssavice

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
import com.ssavice.ssavice.navigation.SsaviceNavHost
import com.ssavice.ssavice.ui.SsaviceBottomBar
import com.ssavice.ui.navigation.SsaviceBaseApp
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
fun SsaviceApp(navController: NavHostController) {
    SsaviceBaseApp(
        navController = navController,
        defaultBottomBar = { navController, route ->
            SsaviceBottomBar(navController, route)
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
