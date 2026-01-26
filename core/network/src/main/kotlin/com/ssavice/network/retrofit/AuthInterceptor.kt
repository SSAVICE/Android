package com.ssavice.network.retrofit

import ErrorCode
import android.util.Log
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.network.AuthEvent
import com.ssavice.network.AuthEventManager
import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.parseAuthError
import errorCodeMap
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

private fun responseCount(response: Response): Int {
    var count = 1
    var prior = response.priorResponse
    while (prior != null) {
        count++
        prior = prior.priorResponse
    }
    return count
}

class AuthInterceptor
    @Inject
    constructor(
        private val tokenRepository: JwtRepository,
        private val authRepository: AuthenticationRepository,
        private val authEventManager: AuthEventManager,
    ) : Authenticator {
        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            if (responseCount(response) >= 2) return null
            return synchronized(this) {
                runBlocking {
                    val refreshFlag = tokenRepository.consumeRefreshFlag()
                    val errorResponse = response.parseAuthError()
                    Log.d("AuthInterceptor", "authenticate: $errorResponse, refresh: $refreshFlag")
                    if (!refreshFlag &&
                        errorCodeMap[errorResponse?.errorProperties?.errorCode] != ErrorCode.EXPIRED_TOKEN
                    ) {
                        authEventManager.emit(AuthEvent.Unauthorized)
                        return@runBlocking null
                    }

                    val refreshResult = authRepository.refreshToken(tokenRepository.getJwt())

                    if (refreshResult.isSuccess) {
                        val token = tokenRepository.getJwt()
                        response.request
                            .newBuilder()
                            .removeHeader(AUTH_HEADER_KEY)
                            .addHeader(AUTH_HEADER_KEY, "Bearer ${token.accessToken}")
                            .build()
                    } else {
                        authEventManager.emit(AuthEvent.Unauthorized)
                        null
                    }
                }
            }
        }

        companion object {
            private const val AUTH_HEADER_KEY = "Authorization"
        }
    }
