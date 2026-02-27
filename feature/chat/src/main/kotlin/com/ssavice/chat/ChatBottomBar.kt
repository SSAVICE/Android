package com.ssavice.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    viewModel: ChattingViewModel = hiltViewModel(),
) {
    Row(modifier = modifier) {
        val state = rememberTextFieldState()

        TextField(
            state = state,
            modifier = Modifier.weight(6f),
        )

        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                viewModel.sendChat(state.text.toString())
                state.clearText()
            },
        ) {
            Text("전송")
        }
    }
}
