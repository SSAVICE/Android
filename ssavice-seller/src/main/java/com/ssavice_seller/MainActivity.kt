package com.ssavice_seller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssavice.designsystem.component.SsaviceTopBar
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.seller_register.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SsaviceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { SsaviceTopBar("Ssavice") },
                ) { innerPadding ->
                    RegisterScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SsaviceTheme {
        Greeting("Android")
    }
}
