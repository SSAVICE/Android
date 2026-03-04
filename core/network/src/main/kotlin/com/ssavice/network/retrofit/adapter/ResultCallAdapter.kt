package com.ssavice.network.retrofit.adapter

import com.ssavice.network.NetworkEventManager
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class ResultCallAdapter<T> (
    private val responseType: Type,
    private val networkEventManager: NetworkEventManager) : CallAdapter<T, Call<Result<T>>> {
    override fun responseType(): Type = responseType
    override fun adapt(call: Call<T>): Call<Result<T>> = ResultCall(call, networkEventManager)
}

class ResultCallAdapterFactory @Inject constructor
    (private val networkEventManager: NetworkEventManager): CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // 반환 타입이 Call이어야 함
        if (getRawType(returnType) != Call::class.java) return null

        // Call의 제네릭 타입이 Result여야 함
        val resultType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(resultType) != Result::class.java) return null

        // Result의 제네릭 타입 추출 (T)
        val responseType = getParameterUpperBound(0, resultType as ParameterizedType)
        return ResultCallAdapter<Any>(responseType, networkEventManager)
    }
}
