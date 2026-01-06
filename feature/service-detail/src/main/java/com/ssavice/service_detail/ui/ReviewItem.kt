package com.ssavice.service_detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ReviewItem(
    review: String,
    rating: Int,
    userName: String,
    date: String,
    serviceName: String,
) {
    ElevatedCard(
        modifier =
            Modifier.Companion
                .fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.Companion.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Companion.Top) {
                Text(
                    modifier = Modifier.Companion.weight(1f),
                    text = userName,
                    fontWeight = FontWeight.Companion.Bold,
                )
                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            modifier = Modifier.Companion.size(18.dp),
                            imageVector = if (index < rating) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (index < rating) MaterialTheme.colorScheme.primary else Color.Companion.Gray,
                        )
                    }
                }
            }
            Text(serviceName, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.Companion.height(4.dp))
            Text(
                date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.Companion.height(6.dp))
            Text(review)
        }
    }
}
