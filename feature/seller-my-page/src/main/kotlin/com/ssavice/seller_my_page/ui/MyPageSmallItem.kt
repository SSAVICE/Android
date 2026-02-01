package com.ssavice.seller_my_page.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MyPageSmallItem(
    icon: ImageVector,
    title: String,
    red: Boolean = false,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        modifier =
            Modifier.Companion
                .fillMaxWidth()
                .height(50.dp)
                .padding(5.dp),
        onClick = onClick,
        colors =
            if (!red) {
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            } else {
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier =
                Modifier.Companion
                    .padding(8.dp)
                    .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Companion.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                modifier = Modifier.Companion.size(16.dp),
                contentDescription = title,
                tint = if (red) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.Companion.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Companion.Bold,
            )
        }
    }
}
