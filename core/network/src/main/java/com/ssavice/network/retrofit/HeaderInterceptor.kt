package com.ssavice.network.retrofit

import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.authentication.TokenRepository
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor @Inject constructor(private val tokenRepository: TokenRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().headers[SKIP_AUTH_KEY] == SKIP_AUTH_VALUE) {
            val newRequest = chain.request().newBuilder()
                .removeHeader(SKIP_AUTH_KEY)
                .build()
            return chain.proceed(newRequest)
        }

        if(tokenRepository.isTokenExpired()) {
            tokenRepository.markRefreshNeeded()
        }

        val newRequest = chain.request().newBuilder()
            .apply {
                header(AUTH_HEADER_KEY, "Bearer ${tokenRepository.getJWT()}")
            }
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val AUTH_HEADER_KEY = "Authorization"
        private const val SKIP_AUTH_KEY = "Auth"
        private const val SKIP_AUTH_VALUE = "false"
    }
}
