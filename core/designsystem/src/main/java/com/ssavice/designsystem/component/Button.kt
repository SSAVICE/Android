package com.ssavice.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.SsaviceTheme

@Composable
private fun SsaviceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        contentPadding = contentPadding,
        content = content,
        shape = RoundedCornerShape(12),
    )
}

@Composable
fun SsaviceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String?,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    SsaviceButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding =
            if (leadingIcon != null) {
                ButtonDefaults.ButtonWithIconContentPadding
            } else {
                ButtonDefaults.ContentPadding
            },
    ) {
        ButtonContent(
            text = { Text(text ?: "") },
            leadingIcon = leadingIcon,
        )
    }
}

@Composable
fun SsaviceButtonOutlined(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String?,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentPadding = ButtonDefaults.ContentPadding,
        shape = RoundedCornerShape(12),
        content = { Text(text ?: "") },
        border = ButtonDefaults.outlinedButtonBorder(enabled).copy(width = 0.4.dp),
    )
}

@Composable
private fun ButtonContent(
    text: @Composable () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    if (leadingIcon != null) {
        Box(Modifier.Companion.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            leadingIcon()
        }
    }
    Box(
        Modifier.Companion
            .padding(
                start =
                    if (leadingIcon != null) {
                        ButtonDefaults.IconSpacing
                    } else {
                        0.dp
                    },
            ),
    ) {
        text()
    }
}

@Preview
@Composable
fun ButtonPreview() {
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(170.dp, 70.dp)) {
            SsaviceButton(
                onClick = {},
                text = "다음",
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}

@Preview
@Composable
fun OutlinedButtonPreview() {
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(170.dp, 70.dp)) {
            SsaviceButtonOutlined(
                onClick = {},
                text = "이전",
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}

@Preview
@Composable
fun ButtonLeadingIconPreview() {
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(170.dp, 70.dp)) {
            SsaviceButton(
                onClick = {},
                text = "Test Button",
                leadingIcon = { Icon(imageVector = Icons.Rounded.Add, contentDescription = null) },
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}
