package com.ssavice.network.retrofit

import com.ssavice.network.authentication.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor
@Inject
constructor(
    private val tokenRepository: TokenRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (tokenRepository.isTokenExpired()) {
            tokenRepository.markRefreshNeeded()
        }

        val newRequest =
            chain
                .request()
                .newBuilder()
                .apply {
                    header(AUTH_HEADER_KEY, "Bearer ${tokenRepository.getJWT().accessToken}")
                }.build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val AUTH_HEADER_KEY = "Authorization"
    }
}
