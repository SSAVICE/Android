package com.ssavice.authentication.repositoryimpl.demo

import com.ssavice.model.TimeStamp
import com.ssavice.model.auth.Jwt
import com.ssavice.network.authentication.AuthenticationRepository
import javax.inject.Inject

class DemoAuthenticationRepository
@Inject constructor() : AuthenticationRepository {

    override suspend fun refreshToken(jwt: Jwt): Jwt? = null

    override suspend fun userLoginWithAccessToken(accessToken: String): Result<Jwt> {
        return Result.success(
            Jwt(
                accessToken = "token",
                refreshToken = "refreshToken",
                accessTokenExpiresAt = TimeStamp(System.currentTimeMillis() + 1000 * 60 * 60)
            )
        )
    }

    override suspend fun companyLoginWithAccessToken(accessToken: String): Result<Jwt> {
        return Result.success(
            Jwt(
                accessToken = "token",
                refreshToken = "refreshToken",
                accessTokenExpiresAt = TimeStamp(System.currentTimeMillis() + 1000 * 60 * 60)
            )
        )
    }
}
