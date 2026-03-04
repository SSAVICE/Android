package com.ssavice.network.retrofit.adapter

import com.ssavice.network.NetworkEvent
import com.ssavice.network.NetworkEventManager
import com.ssavice.network.exception.NetworkUnavailableException
import com.ssavice.network.exception.ServerInternalErrorException
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ResultCall<T>(
    private val delegate: Call<T>,
    private val networkEventManager: NetworkEventManager,
) : Call<Result<T>> {
    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(
            object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>,
                ) {
                    val result =
                        if (response.isSuccessful) {
                            val body = response.body()
                            // Unit 타입이거나 바디가 있는 경우 처리
                            if (body == null && response.code() in 200..299) {
                                @Suppress("UNCHECKED_CAST")
                                Result.success(Unit as T)
                            } else if (body != null) {
                                Result.success(body)
                            } else {
                                Result.failure(Exception("응답 데이터가 비어있습니다."))
                            }
                        } else {
                            // 기존 ResponseProcessing의 에러 처리 로직 통합
                            val error =
                                when (response.code()) {
                                    500 -> ServerInternalErrorException(response.message())
                                    else -> Exception("HTTP ${response.code()}: ${response.message()}")
                                }
                            Result.failure(error)
                        }
                    callback.onResponse(this@ResultCall, Response.success(result))
                }

                override fun onFailure(
                    call: Call<T>,
                    t: Throwable,
                ) {
                    val error =
                        when (t) {
                            is IOException -> {
                                networkEventManager.tryEmit(NetworkEvent.NetworkUnavailable)
                                NetworkUnavailableException("네트워크 연결 실패: ${t.localizedMessage}")
                            }

                            else -> {
                                t
                            }
                        }
                    callback.onResponse(this@ResultCall, Response.success(Result.failure(error)))
                }
            },
        )
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun execute(): Response<Result<T>> = throw UnsupportedOperationException("ResultCall does not support synchronous execution")

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone(), networkEventManager)
}
