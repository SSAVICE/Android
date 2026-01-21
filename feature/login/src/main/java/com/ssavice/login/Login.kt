package com.ssavice.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.theme.SsaviceTheme

@Composable
fun LoginRoute(
    modifier: Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    isUser: Boolean,
    onLoginComplete: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.loginState) {
        if(state.loginState is LoginState.Success) onLoginComplete()
    }

    LoginPage(
        modifier = modifier,
        onLoginButtonClicked = { viewModel.onLoginButtonClicked(isUser) },
        uiState = state
    )
}

@Composable
fun LoginPage(
    modifier: Modifier,
    onLoginButtonClicked: () -> Unit,
    uiState: LoginUiState
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                enabled = uiState.loginState !is LoginState.Loading,
                onClick = onLoginButtonClicked,
                content = {
                    Text(
                        text = "로그인",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            if(uiState.loginState is LoginState.Error){
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    text = uiState.loginState.message,
                    style = MaterialTheme.typography.labelSmall,
                    softWrap = true
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginPagePreview() {
    val state = LoginUiState(
        loginState = LoginState.Idle
    )
    SsaviceTheme {
        LoginPage(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            onLoginButtonClicked = {},
            uiState = state
        )
    }
}

@Preview
@Composable
fun LoginPageErrorPreview() {
    val state = LoginUiState(
        loginState = LoginState.Error("ErrorThisisLongErrorThisisVeryLongError\n" +
                "ErrorThisisLongErrorThisisVeryLongErrorErrorThisisLongErrorThisi" +
                "sVeryLongErrorErrorThisisLongErrorThisisVeryLongErrorErrorThisisLon" +
                "gErrorThisisVeryLongError\nErrorThisisLongErrorThisisVeryLongErrorErr" +
                "orThisisLongErrorThisisVeryLongErrorErrorThisisLongErrorThisisVeryLongErr" +
                "orErrorThisisLongErrorThisisVeryLongErrorErrorThisisLongErrorThisisVeryL" +
                "ongErrorErrorThisisLongErrorThisisVeryLongError")
    )
    SsaviceTheme {
        LoginPage(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            onLoginButtonClicked = {},
            uiState = state
        )
    }
}
