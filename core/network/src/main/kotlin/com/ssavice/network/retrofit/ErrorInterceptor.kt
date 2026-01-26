package com.ssavice.network.retrofit

import android.util.Log
import com.ssavice.network.AuthEvent
import com.ssavice.network.AuthEventManager
import com.ssavice.network.parseAuthError
import com.ssavice.network.parseError
import errorCodeMap
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ErrorInterceptor @Inject constructor(
    private val authEventManager: AuthEventManager
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        runBlocking {
            val response = chain.proceed(chain.request())

            if(response.isSuccessful) return@runBlocking response

            val errorResponse = response.parseError()

            Log.d(TAG, "intercept: $errorResponse")

            when(errorCodeMap[errorResponse?.errorCode]){
                ErrorCode.FORBIDDEN ->{
                    authEventManager.emit(AuthEvent.Forbidden)
                }
                else -> {}
            }
            return@runBlocking response
        }

    companion object {
        private const val TAG = "ErrorInterceptor"
    }
}
