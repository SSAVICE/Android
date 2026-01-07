package com.ssavice.user_my_page.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.user_my_page.ParticipationState

@Composable
fun ParticipationSummary(participationState: ParticipationState?) {
    @Composable
    fun CardElement(
        modifier: Modifier,
        value: String,
        text: String,
    ) {
        SsaviceElevatedCard(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
            }
        }
    }
    Row(
        modifier =
            Modifier
                .padding(horizontal = 10.dp)
                .height(90.dp)
                .fillMaxWidth(),
        horizontalArrangement = spacedBy(16.dp),
    ) {
        if (participationState != null) {
            CardElement(Modifier.weight(1f), participationState.onProgress.toString(), "진행 중")
            CardElement(Modifier.weight(1f), participationState.done.toString(), "완료")
            CardElement(Modifier.weight(1f), participationState.total.toString(), "전체")
        } else {
            CardElement(Modifier.weight(1f), "-", "진행중")
            CardElement(Modifier.weight(1f), "-", "완료")
            CardElement(Modifier.weight(1f), "-", "전체")
        }
    }
}
