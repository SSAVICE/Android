package com.ssavice.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

@VisibleForTesting
val LightDefaultColorScheme =
    lightColorScheme(
        primary = SsaviceOrange,
        background = Color.White,
        surface = SsaviceSurfaceGray,
        primaryContainer = SsaviceSurfaceVariant,
        onPrimaryContainer = SsaviceOnSurfaceVariant,
    )

@Composable
fun SsaviceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    // Color scheme
    val colorScheme = LightDefaultColorScheme

    val backgroundTheme =
        BackgroundTheme(
            color = colorScheme.surface,
            tonalElevation = 2.dp,
        )

    CompositionLocalProvider(
        LocalBackgroundTheme provides backgroundTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )

        val context = LocalContext.current
        LaunchedEffect(darkTheme) {
            val window = (context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = true
        }
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
