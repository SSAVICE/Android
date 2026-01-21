package com.ssavice.authentication.repositoryimpl.remote

import com.ssavice.authentication.service.AuthRetrofitService
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.model.auth.Jwt
import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.model.LoginDTO
import com.ssavice.network.processResponseOnResponseData
import javax.inject.Inject

class RemoteAuthenticationRepository
    @Inject
    constructor(
        private val authRetrofitService: AuthRetrofitService,
        private val jwtRepository: JwtRepository,
    ) : AuthenticationRepository {
        override suspend fun refreshToken(jwt: Jwt): Result<Unit> {
            val response = authRetrofitService.refreshToken(jwt.refreshToken)
            val result = processResponseOnResponseData(response).map { it.toJwt() }
            result.fold(
                onSuccess = {
                    jwtRepository.setJwt(it)
                    return Result.success(Unit)
                },
                onFailure = { return Result.failure(it) },
            )
        }

        override suspend fun userLoginWithAccessToken(accessToken: String): Result<Unit> {
            val result =
                processResponseOnResponseData(
                    authRetrofitService.userLogin(LoginDTO(accessToken)),
                ).map { it.toJwt() }

            return result.fold(
                onSuccess = {
                    jwtRepository.setJwt(it)
                    Result.success(Unit)
                },
                onFailure = {
                    Result.failure(it)
                },
            )
        }

        override suspend fun companyLoginWithAccessToken(accessToken: String): Result<Unit> {
            val result =
                processResponseOnResponseData(
                    authRetrofitService.companyLogin(LoginDTO(accessToken)),
                ).map { it.toJwt() }

            return result.fold(
                onSuccess = {
                    jwtRepository.setJwt(it)
                    Result.success(Unit)
                },
                onFailure = {
                    Result.failure(it)
                },
            )
        }
    }
