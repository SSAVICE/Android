package com.ssavice.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LabeledComponent(
    modifier: Modifier = Modifier,
    labelText: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    body: @Composable () -> Unit
) {
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(isError) {
        if (isError) {
            offsetX.animateTo(0f) // Reset position

            offsetX.animateTo(5f, tween(50))
            offsetX.animateTo(-5f, tween(50))
            offsetX.animateTo(2f, tween(50))
            offsetX.animateTo(-2f, tween(50))
            offsetX.animateTo(0f, tween(50))
        }
    }

    Column(modifier = modifier) {
        Column (modifier = Modifier.offset(x = offsetX.value.dp)) {
            if (labelText != null) {
                ProvideTextStyle(value = MaterialTheme.typography.labelSmall)
                {
                    Text(
                        text = labelText,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier
                            .padding(
                                start = 4.dp,
                                bottom = 4.dp
                            )
                            .alpha(0.85f)
                    )
                }
            }

            body()
        }

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
            )
        }
    }
}
