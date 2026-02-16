package com.ssavice.authentication.repositoryimpl.demo

import com.ssavice.model.auth.Jwt
import com.ssavice.network.authentication.AuthenticationRepository
import javax.inject.Inject

class DemoAuthenticationRepository
    @Inject
    constructor() : AuthenticationRepository {
        override suspend fun refreshToken(jwt: Jwt): Result<Unit> = Result.success(Unit)

        override suspend fun userLoginWithAccessToken(accessToken: String): Result<Unit> = Result.success(Unit)

        override suspend fun companyLoginWithAccessToken(accessToken: String): Result<Unit> = Result.success(Unit)

        override suspend fun logout(): Result<Unit> = Result.success(Unit)
    }
