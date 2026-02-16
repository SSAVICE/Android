package com.ssavice.user_my_page.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.SsaviceElevatedCard

@Composable
fun MyPageItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit = {},
) {
    SsaviceElevatedCard(
        modifier =
            Modifier.Companion
                .fillMaxWidth()
                .defaultMinSize(minHeight = 95.dp)
                .padding(5.dp),
        onClick = onClick,
    ) {
        Column(
            modifier =
                Modifier.Companion
                    .padding(8.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                modifier = Modifier.Companion.size(20.dp),
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Companion.Bold,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}
