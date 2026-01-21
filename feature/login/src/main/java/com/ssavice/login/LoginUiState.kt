package com.ssavice.login

data class LoginUiState(
    val loginState: LoginState = LoginState.Idle,
)

sealed interface LoginState {
    object Loading : LoginState

    data class Error(
        val message: String,
    ) : LoginState

    object Success : LoginState

    object Idle : LoginState
}
