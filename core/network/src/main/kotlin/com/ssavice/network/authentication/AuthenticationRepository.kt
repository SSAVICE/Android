package com.ssavice.network.authentication

import com.ssavice.model.auth.Jwt

interface AuthenticationRepository {
    suspend fun refreshToken(jwt: Jwt): Result<Unit>

    suspend fun userLoginWithAccessToken(accessToken: String): Result<Unit>

    suspend fun companyLoginWithAccessToken(accessToken: String): Result<Unit>
}
