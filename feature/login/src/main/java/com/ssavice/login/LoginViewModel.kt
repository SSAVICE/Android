package com.ssavice.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.ssavice.login.navigation.LoginNavigationContract
import com.ssavice.network.authentication.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState(
        loginState = LoginState.CheckAutoLogin,
        isUser = savedStateHandle[LoginNavigationContract.IS_USER] ?:throw IllegalStateException("isUser is null")
    ))
    val uiState = _uiState.asStateFlow()

    fun onLoginButtonClicked() {
        val accessToken = "1234" // TODO: NEED TO IMPLEMENT ACCESS TOKEN
        _uiState.update {
            it.copy(loginState = LoginState.OnLogin)
        }
        viewModelScope.launch {
            val result =
                if (_uiState.value.isUser) {
                    authenticationRepository.userLoginWithAccessToken(accessToken)
                } else {
                    authenticationRepository.companyLoginWithAccessToken(accessToken)
                }

            result
                .onSuccess {
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

    private fun login(kakaoAccessToken: String) {
        _uiState.update {
            it.copy(loginState = LoginState.OnLogin)
        }
        viewModelScope.launch {
            val result =
                if (uiState.value.isUser) {
                    authenticationRepository.userLoginWithAccessToken(kakaoAccessToken)
                } else {
                    authenticationRepository.companyLoginWithAccessToken(kakaoAccessToken)
                }

            result
                .onSuccess {
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

    private fun requestLogin() {
        _uiState.update {
            it.copy(loginState = LoginState.NeedLogin)
        }
    }

    fun tryAutoLogin() {
        if (!AuthApiClient.instance.hasToken()) {
            requestLogin()
            return
        }

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.d(TAG, "토큰 정보 조회 실패", error)
                requestLogin()
            } else if (tokenInfo != null) {
                Log.d(TAG, "토큰 정보 조회 성공 $tokenInfo")
                val token = AuthApiClient.instance.tokenManagerProvider.manager.getToken()
                if(token!= null) {
                    Log.d(TAG, "토큰 조회 성공 ${token.accessToken}")
                    login(token.accessToken)
                }
                else{
                    Log.d(TAG, "토큰 조회 실패", error)
                    requestLogin()
                }
            }
        }
    }

    fun onKakaoLoginSuccess(token: String) {
        login(token)
    }

    fun onKakaoLoginError(error: Throwable) {
        _uiState.update {
            it.copy(loginState = LoginState.Error(error.stackTraceToString()))
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
