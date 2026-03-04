package com.ssavice.network.retrofit

import android.util.Log
import com.ssavice.network.NetworkEvent
import com.ssavice.network.NetworkEventManager
import com.ssavice.network.parseError
import errorCodeMap
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ErrorInterceptor
    @Inject
    constructor(
        private val networkEventManager: NetworkEventManager,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response =
            runBlocking {
                val response = chain.proceed(chain.request())

                if (response.isSuccessful) return@runBlocking response

                val errorResponse = response.parseError()

                Log.d(TAG, "intercept: $errorResponse")

                when (errorCodeMap[errorResponse?.errorCode]) {
                    ErrorCode.FORBIDDEN -> {
                        networkEventManager.emit(NetworkEvent.Forbidden)
                    }

                    ErrorCode.COMPANY_NOT_FOUND -> {
                        networkEventManager.emit(NetworkEvent.Initial)
                    }

                    else -> {}
                }
                return@runBlocking response
            }

        companion object {
            private const val TAG = "ErrorInterceptor"
        }
    }
