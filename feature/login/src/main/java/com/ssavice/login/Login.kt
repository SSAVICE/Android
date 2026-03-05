package com.ssavice.login

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ssavice.core.network.BuildConfig
import com.ssavice.designsystem.theme.SsaviceGradientBackground
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.feature.login.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    modifier: Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginComplete: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(state.loginState) {
        when (state.loginState) {
            LoginState.CheckAutoLogin -> {
                viewModel.tryAutoLogin()
            }

            LoginState.Success -> {
                onLoginComplete()
            }

            else -> {}
        }
    }

    LoginPage(
        modifier = modifier,
        onLoginButtonClicked = {
            loginWithKakaoAccount(
                context = context,
                onSuccess = viewModel::onKakaoLoginSuccess,
                onError = viewModel::onKakaoLoginError,
            )
        },
        uiState = state,
    )
}

@Composable
fun LoginPage(
    modifier: Modifier,
    onLoginButtonClicked: () -> Unit,
    uiState: LoginUiState,
) {
    Box(
        modifier = modifier.background(SsaviceGradientBackground),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TitleSpace(
                Modifier
                    .fillMaxWidth()
                    .height(600.dp),
            )
            if (uiState.loginState == LoginState.NeedLogin) {
                LoginSpace(
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    onLoginButtonClicked = onLoginButtonClicked,
                    uiState = uiState,
                )
            }
            if (BuildConfig.DEBUG) {
                HashKeySpace(
                    Modifier.weight(0.5f),
                )
            }
        }
    }
}

@Composable
fun TitleSpace(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var startAnimation by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(300)
            startAnimation = true
        }

        FadingText(
            delay = 0,
            startAnimation = startAnimation,
        ) { modifier ->
            Text(
                modifier = modifier,
                text = "이곳에 슬로건 삽입",
                style =
                    MaterialTheme.typography.displaySmall.copy(
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        FadingText(
            delay = 600,
            startAnimation = startAnimation,
        ) { modifier ->
            Text(
                modifier = modifier,
                text = "SSAVICE",
                style =
                    MaterialTheme.typography.displayLarge.copy(
                        letterSpacing = 12.sp,
                        fontWeight = FontWeight.Black,
                    ),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

private fun loginWithKakaoTalk(
    context: Context,
    onSuccess: (String) -> Unit,
    onError: (Throwable) -> Unit,
) {
    UserApiClient.instance.loginWithKakaoTalk(
        context,
    ) { token, error ->
        if (error != null) {
            Log.d(TAG, "로그인 실패", error)

            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                return@loginWithKakaoTalk
            } else if (
                error is ClientError &&
                error.reason == ClientErrorCause.NotSupported
            ) {
                loginWithKakaoAccount(context, onSuccess, onError)
            } else {
                onError(error)
            }
        } else if (token != null) {
            Log.d(TAG, "로그인 성공 ${token.accessToken}")
            onSuccess(token.accessToken)
        }
    }
}

private fun loginWithKakaoAccount(
    context: Context,
    onSuccess: (String) -> Unit,
    onError: (Throwable) -> Unit) {
    UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
            return@loginWithKakaoAccount
        } else if (token != null) {
            Log.d(TAG, "로그인 성공 ${token.accessToken}")
            onSuccess(token.accessToken)
        }else if (error != null) {
            onError(error)
        }
    }
}

@Composable
fun FadingText(
    delay: Int,
    startAnimation: Boolean,
    content: @Composable (Modifier) -> Unit,
) {
    val offset by animateDpAsState(
        targetValue = if (startAnimation) (-20).dp else 0.dp,
        animationSpec =
            tween(
                durationMillis = 2000,
                delayMillis = delay,
                easing = EaseOutCubic,
            ),
        label = "SloganOffset",
    )
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec =
            tween(
                durationMillis = 2000,
                delayMillis = delay,
                easing = EaseOutCubic,
            ),
        label = "Alpha",
    )

    content(
        Modifier
            .offset(y = offset)
            .alpha(alpha),
    )
}

@Composable
fun LoginSpace(
    modifier: Modifier = Modifier,
    onLoginButtonClicked: () -> Unit,
    uiState: LoginUiState,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(width = 320.dp, height = 65.dp)
                    .clickable(
                        // 1. 클릭 시 시각적 효과(리플)를 제거하기 위해 indication을 null로 설정
                        indication = null,
                        // 2. 상호작용 상태를 추적할 source (필수 입력)
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onLoginButtonClicked,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                painter = painterResource(id = R.drawable.kakao_login_medium_wide),
                contentDescription = "Login With Kakao",
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp),
        )
        if (uiState.loginState is LoginState.Error) {
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                text = uiState.loginState.message,
                style = MaterialTheme.typography.labelSmall,
                softWrap = true,
            )
        }
    }
}

@Composable
fun HashKeySpace(modifier: Modifier) {
    val hashText = KakaoSdk.keyHash
    val clipboardManager = LocalClipboard.current
    val context = LocalContext.current
    Column(modifier = modifier) {
        Column(
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                )
                .clickable {
                    val data = android.content.ClipData.newPlainText("Hash", hashText)
                    CoroutineScope(Dispatchers.Main).launch {
                        clipboardManager.setClipEntry(
                            data.toClipEntry(),
                        )
                        android.widget.Toast
                            .makeText(
                                context,
                                "해시 키가 복사되었습니다.",
                                android.widget.Toast.LENGTH_SHORT,
                            ).show()
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("해시 키: \n$hashText")
        }
    }
}

@Preview
@Composable
fun LoginPagePreview() {
    val state =
        LoginUiState(
            loginState = LoginState.Idle,
            isUser = true,
        )
    SsaviceTheme {
        LoginPage(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            onLoginButtonClicked = {},
            uiState = state,
        )
    }
}

@Preview
@Composable
fun LoginPageErrorPreview() {
    val state =
        LoginUiState(
            loginState =
                LoginState.Error(
                    "ErrorThisisLongErrorThisisVeryLongError\n" +
                            "ErrorThisisLongErrorThisisVeryLongErrorErrorThisisLongErrorThisi" +
                            "sVeryLongErrorErrorThisisLongErrorThisisVeryLongErrorErrorThisisLon" +
                            "gErrorThisisVeryLongError\nErrorThisisLongErrorThisisVeryLongErrorErr" +
                            "orThisisLongErrorThisisVeryLongErrorErrorThisisLongErrorThisisVeryLongErr" +
                            "orErrorThisisLongErrorThisisVeryLongErrorErrorThisisLongErrorThisisVeryL" +
                            "ongErrorErrorThisisLongErrorThisisVeryLongError",
                ),
            isUser = true,
        )
    SsaviceTheme {
        LoginPage(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            onLoginButtonClicked = {},
            uiState = state,
        )
    }
}

private const val TAG = "LoginScreen"
