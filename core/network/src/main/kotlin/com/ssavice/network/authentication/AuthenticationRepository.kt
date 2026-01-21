package com.ssavice.network.authentication

import com.ssavice.model.auth.Jwt

interface AuthenticationRepository {
    suspend fun refreshToken(jwt: Jwt): Jwt?

    suspend fun userLoginWithAccessToken(accessToken: String): Result<Jwt>

    suspend fun companyLoginWithAccessToken(accessToken: String): Result<Jwt>
}
