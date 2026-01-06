package com.ssavice.user_main

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserMainTopBar(
    viewModel: UserMainViewModel
) {
    TopAppBar(
        title = {
            TextButton (
                onClick =  viewModel::onSetLocationClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
            ) {
                Text(
                    text = "달서구 송현1동",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "위치 선택")
            }
        },
        actions = {
            IconButton(onClick = viewModel::onNotificationButtonClick) {
                Icon(Icons.Default.Notifications,
                    contentDescription = "알림")
            }
        }
    )
}
