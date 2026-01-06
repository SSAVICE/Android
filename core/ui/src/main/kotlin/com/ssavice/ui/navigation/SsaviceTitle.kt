package com.ssavice.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SsaviceTitle(
    title: String,
    onBackButtonClick: (() -> Unit)?,
    action: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            if (onBackButtonClick != null) {
                IconButton(
                    onClick = onBackButtonClick,
                ) {
                    Icon(
                        Icons.Filled.ArrowBackIosNew,
                        contentDescription = "뒤로",
                    )
                }
            } else {
                null
            }
        },
        actions = { action() },
    )
}
