package com.ssavice.network.retrofit

import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.model.auth.Jwt
import com.ssavice.network.authentication.AuthenticationRepository
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
) : Authenticator {
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        if (responseCount(response) >= 2) return null
        return synchronized(this) {
            runBlocking {
                if (!tokenRepository.consumeRefreshFlag()
                    && response.code != 401
                ) {
                    return@runBlocking null
                }

                val newToken = authRepository.refreshToken(tokenRepository.getJwt()) ?: Jwt.EMPTY

                if (newToken != Jwt.EMPTY) {
                    tokenRepository.setJwt(newToken)
                    response.request
                        .newBuilder()
                        .removeHeader(AUTH_HEADER_KEY)
                        .addHeader(AUTH_HEADER_KEY, "Bearer ${newToken.accessToken}")
                        .build()
                } else {
                    null
                }
            }
        }
    }

    companion object {
        private const val AUTH_HEADER_KEY = "Authorization"
    }
}
