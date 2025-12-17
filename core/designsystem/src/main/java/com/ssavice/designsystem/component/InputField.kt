package com.ssavice.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.SsaviceLightGray
import com.ssavice.designsystem.theme.SsaviceTheme

@Composable
fun SsaviceInputField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholderText: String? = null,
    labelText: String? = null,
    multiLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(8.dp)
    OutlinedTextField(
        state = state,
        modifier = modifier,
        shape = shape,
        lineLimits =
            if (multiLine) TextFieldLineLimits.SingleLine
            else TextFieldLineLimits.MultiLine(
                1,
                4
            ),
        placeholder = { PlaceHolderText(placeholderText ?: "") },
        contentPadding = PaddingValues(12.dp),
        label = {
            if (labelText != null) {
                ProvideTextStyle(value = MaterialTheme.typography.labelSmall)
                { Text(labelText) }
            } else null
        },
        labelPosition = TextFieldLabelPosition.Above(),
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        colors = OutlinedTextFieldDefaults.colors().copy(
            unfocusedIndicatorColor = SsaviceLightGray
        )
    )
}

@Preview
@Composable
private fun InputFieldPreview() {
    val state = remember {
        TextFieldState()
    }
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(170.dp, 70.dp)) {
            SsaviceInputField(
                modifier = Modifier.padding(10.dp),
                state = state,
                placeholderText = "이름"
            )
        }
    }
}

@Preview
@Composable
private fun LabeledInputFieldPreview() {
    val state = remember {
        TextFieldState()
    }
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(200.dp, 150.dp)) {
            Box(
                modifier = Modifier
                    .size(170.dp, 120.dp)
                    .padding(10.dp)
            ) {
                SsaviceInputField(
                    state = state,
                    placeholderText = "홍길동",
                    labelText = "이름"
                )
            }
        }
    }
}

@Preview
@Composable
private fun LabeledInputFieldMultilinePreview() {
    val state = remember {
        TextFieldState(
            "ABCDEFG\n대한민국 최고의 중식 전문점 최고의 맛을 보장합니다.\n예약문의: 010-1234-5678\n주차가능"
        )
    }
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(350.dp, 600.dp)) {
            Box(
                modifier = Modifier
                    .size(170.dp, 120.dp)
                    .padding(10.dp)
            ) {
                SsaviceInputField(
                    state = state,
                    placeholderText = "홍길동",
                    labelText = "설명"
                )
            }
        }
    }
}

@Composable
private fun PlaceHolderText(text: String) {
    Text(
        text = text,
        style = Typography().bodyMedium,
        modifier = Modifier
            .wrapContentHeight(align = Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    )
}
