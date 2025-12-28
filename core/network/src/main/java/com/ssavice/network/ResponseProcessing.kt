package com.ssavice.network

import android.util.Log
import com.ssavice.network.exception.ServerInternalErrorException
import retrofit2.Response

private fun <T> isResponseError(response: Response<T>): Boolean = (response.code() in 400 until 500)

fun processResponse(response: Response<Unit>): Result<Unit> {
    Log.d(
        "KSC",
        "ProcessExpenseDetail code: ${response.code()}, message: ${response.message()}",
    )

    if (isResponseError(response)) {
        return when (response.code()) {
            500 -> Result.failure(ServerInternalErrorException(response.message()))
            else -> Result.failure(ServerInternalErrorException(response.message()))
        }
    }

    return if (isResponseValid(response)) {
        return Result.success(Unit)
    } else {
        Result.failure(Exception("알 수 없는 오류 발생: ${response.message()}"))
    }
}

fun <T> processResponse(response: Response<T>): Result<T> {
    Log.d(
        "KSC",
        "ProcessExpenseDetail code: ${response.code()}, message: ${response.message()}",
    )
    if (isResponseError(response)) {
        return when (response.code()) {
            500 -> Result.failure(ServerInternalErrorException(response.message()))
            else -> Result.failure(ServerInternalErrorException(response.message()))
        }
    }

    return response.body()?.let {
        Result.success(it)
    } ?: Result.failure(Exception("비어 있는 응답 데이터: ${response.message()}"))
}

private fun <T> isResponseValid(response: Response<T>): Boolean = (response.code() in 200 until 300)
