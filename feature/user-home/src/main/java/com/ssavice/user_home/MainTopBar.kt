package com.ssavice.user_home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeTopBar(viewModel: UserHomeViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TopAppBar(
        title = {
            TextButton(
                onClick = viewModel::onLocationClick,
                colors =
                    ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(28.dp),
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "위치 아이콘"
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        modifier = Modifier.weight(1f),
                        text = (state.addressState as? RegionState.Showing)?.address ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        fontWeight = FontWeight.SemiBold,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.width(5.dp))
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "",
                    )
                }
            }
        },
        actions = {
            Spacer(Modifier.width(20.dp))
            // IconButton(onClick = viewModel::onNotificationButtonClick) {
            //     Icon(
            //         Icons.Default.Notifications,
            //         contentDescription = "알림",
            //     )
            // } TODO: 현재 비 활성화
        },
    )
}
