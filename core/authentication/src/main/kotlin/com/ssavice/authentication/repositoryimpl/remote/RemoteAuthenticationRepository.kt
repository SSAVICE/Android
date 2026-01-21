package com.ssavice.authentication.repositoryimpl.remote

import com.ssavice.model.auth.Jwt
import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.model.LoginDTO
import com.ssavice.network.processResponseOnResponseData
import com.ssavice.authentication.service.AuthRetrofitService
import javax.inject.Inject

class RemoteAuthenticationRepository @Inject constructor(
    private val authRetrofitService: AuthRetrofitService
) : AuthenticationRepository {
    override suspend fun refreshToken(jwt: Jwt): Jwt? {
        val response = authRetrofitService.refreshToken(jwt.refreshToken)
        val result = processResponseOnResponseData(response)
        result.fold(
            onSuccess = { return it.toJwt() },
            onFailure = { return null }
        )
    }

    override suspend fun userLoginWithAccessToken(accessToken: String): Result<Jwt> =
        processResponseOnResponseData(
            authRetrofitService.userLogin(LoginDTO(accessToken))
        ).map { it.toJwt() }


    override suspend fun companyLoginWithAccessToken(accessToken: String): Result<Jwt> =
        processResponseOnResponseData(
            authRetrofitService.companyLogin(LoginDTO(accessToken))
        ).map { it.toJwt() }
}
