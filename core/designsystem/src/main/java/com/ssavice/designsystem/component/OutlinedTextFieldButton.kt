package com.ssavice.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.SsaviceRoundRectShape

@Composable
fun OutlinedTextFieldButton(
    modifier: Modifier = Modifier,
    placeHolder: String,
    text: String,
    trailingIcon: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = {}, // Read-only, so this does nothing
            readOnly = true,
            modifier =
                Modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
            label = {
                ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                    Text(
                        text = placeHolder,
                        modifier =
                            Modifier
                                .padding(0.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically),
                    )
                }
            },
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            shape = SsaviceRoundRectShape,
        )

        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .clickable(
                        onClick = onClick,
                        indication = null, // No ripple effect
                        interactionSource = remember { MutableInteractionSource() },
                    ),
        )
    }
}
