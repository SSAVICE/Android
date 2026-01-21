package com.ssavice.authentication.service

import com.ssavice.network.model.JwtDTO
import com.ssavice.network.model.LoginDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRetrofitService {
    @POST("/api/user/login")
    suspend fun userLogin(
        @Body body: LoginDTO,
    ): Response<JwtDTO>

    @POST("/api/company/login")
    suspend fun companyLogin(
        @Body body: LoginDTO,
    ): Response<JwtDTO>

    @GET("/api/auth/token/refresh")
    suspend fun refreshToken(
        @Header(REFRESH_TOKEN_HEADER_KEY) refreshToken: String,
    ): Response<JwtDTO>

    @GET("/api/auth/logout")
    suspend fun logout(
        @Header(REFRESH_TOKEN_HEADER_KEY) refreshToken: String,
    ): Response<Unit>

    companion object {
        const val REFRESH_TOKEN_HEADER_KEY = "X-Refresh-Token"
    }
}
