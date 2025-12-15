package com.ssavice.network.retrofit

import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.authentication.TokenRepository
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

class AuthInterceptor @Inject constructor(private val tokenRepository: TokenRepository, private val authRepository: AuthenticationRepository) :
    Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            if(!tokenRepository.consumeRefreshFlag() && responseCount(response)>=2) {
                return null
            }

            // 새로운 토큰을 저장
            val newToken = authRepository.refreshToken(tokenRepository.getJWT())
                ?: return null

            runBlocking {
                tokenRepository.updateJwt(newToken)
            }

            return response.request.newBuilder()
                .removeHeader(AUTH_HEADER_KEY)
                .addHeader(AUTH_HEADER_KEY, "Bearer ${newToken.accessToken}")
                .build()
        }
    }

    companion object {
        private const val AUTH_HEADER_KEY = "Authorization"
    }
}
