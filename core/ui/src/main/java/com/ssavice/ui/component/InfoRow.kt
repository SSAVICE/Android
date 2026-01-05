package com.ssavice.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    icon: Painter,
    iconContentDescription: String?,
    title: String,
    content: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = icon,
            contentDescription = iconContentDescription,
            tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            Text(content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconContentDescription: String?,
    title: String,
    content: String
) {
    InfoRow(
        modifier = modifier,
        icon = rememberVectorPainter(icon),
        iconContentDescription = iconContentDescription,
        title = title,
        content = content
    )
}
