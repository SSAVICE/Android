package com.ssavice.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
) {
    val state = rememberTextFieldState()
    ChatBottomBar(
        modifier = modifier,
        inputText = state,
        onSendClick = {
            if (state.text.isNotBlank()) {
                viewModel.sendChat(state.text.toString())
                state.clearText()
            }
        },
    )
}

@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    inputText: TextFieldState,
    onSendClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent,
        tonalElevation = 3.dp, // 하단 바를 살짝 띄워줌
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .navigationBarsPadding() // 네비게이션 바 영역 확보
                    .imePadding(),
            // 키보드가 올라올 때 여백 확보
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // [라운드 렉트 입력창]
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            MaterialTheme.colorScheme.primaryContainer.copy(
                                alpha = 0.5f,
                            ),
                        ).padding(horizontal = 16.dp, vertical = 10.dp),
            ) {
                BasicTextField(
                    state = inputText,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle =
                        TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                        ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    decorator = { innerTextField ->
                        if (inputText.text.isEmpty()) {
                            Text(
                                text = "메시지를 입력하세요...",
                                style =
                                    TextStyle(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                        fontSize = 16.sp,
                                    ),
                            )
                        }
                        innerTextField()
                    },
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // [전송 버튼] - 아이콘 버튼 형태가 더 세련됨
            IconButton(
                onClick = onSendClick,
                enabled = inputText.text.isNotBlank(),
                colors =
                    IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.outline,
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "전송",
                    modifier = Modifier.size(28.dp).padding(horizontal = 4.dp),
                )
            }
        }
    }
}
