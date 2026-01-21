package com.ssavice.network.retrofit

import com.ssavice.datastore.repository.JwtRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor
    @Inject
    constructor(
        private val tokenRepository: JwtRepository,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = runBlocking {
                tokenRepository.getJwt()
            }
            if (token.isExpired()) {
                tokenRepository.markRefreshNeeded()
            }

            val newRequest =
                chain
                    .request()
                    .newBuilder()
                    .apply {
                        header(AUTH_HEADER_KEY, "Bearer ${token.accessToken}")
                    }.build()

            return chain.proceed(newRequest)
        }

        companion object {
            private const val AUTH_HEADER_KEY = "Authorization"
        }
    }
