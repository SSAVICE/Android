package com.ssavice.network.authentication.demo

import com.ssavice.model.TimeStamp
import com.ssavice.model.auth.Jwt
import com.ssavice.network.authentication.AuthenticationRepository
import retrofit2.Response
import javax.inject.Inject

class DemoAuthenticationRepository
@Inject constructor() : AuthenticationRepository {

    override suspend fun refreshToken(jwt: Jwt): Jwt? = null

    override suspend fun userLoginWithAccessToken(accessToken: String): Response<Jwt> {
        return Response.success(
            Jwt(
                accessToken = "token",
                refreshToken = "refreshToken",
                accessTokenExpiresAt = TimeStamp(System.currentTimeMillis() + 1000 * 60 * 60)
            )
        )
    }

    override suspend fun companyLoginWithAccessToken(accessToken: String): Response<Jwt> {
        return Response.success(
            Jwt(
                accessToken = "token",
                refreshToken = "refreshToken",
                accessTokenExpiresAt = TimeStamp(System.currentTimeMillis() + 1000 * 60 * 60)
            )
        )
    }
}
