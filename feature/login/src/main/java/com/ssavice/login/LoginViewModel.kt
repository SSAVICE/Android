package com.ssavice.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.network.authentication.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onLoginButtonClicked(isUser: Boolean) {
        val accessToken = "1234"        // TODO: NEED TO IMPLEMENT ACCESS TOKEN
        _uiState.update {
            it.copy(loginState = LoginState.Loading)
        }
        viewModelScope.launch {
            val result = if (isUser)
                authenticationRepository.userLoginWithAccessToken(accessToken)
            else
                authenticationRepository.companyLoginWithAccessToken(accessToken)

            result.onSuccess {
                _uiState.update {
                    it.copy(loginState = LoginState.Success)
                }
            }.onFailure { message ->
                _uiState.update {
                    it.copy(loginState = LoginState.Error(message.stackTraceToString()))
                }
            }
        }
    }
}
