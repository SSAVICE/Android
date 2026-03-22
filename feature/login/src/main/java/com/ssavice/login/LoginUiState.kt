package com.ssavice.login

data class LoginUiState(
    val loginState: LoginState = LoginState.CheckAutoLogin,
    val isUser: Boolean,
)

sealed interface LoginState {
    object CheckAutoLogin : LoginState

    object NeedLogin : LoginState

    object OnLogin : LoginState

    data class Error(
        val message: String,
    ) : LoginState

    object Success : LoginState

    object Idle : LoginState

    object NotAvailable : LoginState
}
