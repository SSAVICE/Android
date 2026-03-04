package com.ssavice.authentication.repositoryimpl.remote

import android.content.Context
import android.util.Log
import com.kakao.sdk.user.UserApiClient
import com.ssavice.authentication.service.AuthRetrofitService
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.model.auth.Jwt
import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.model.LoginDTO
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RemoteAuthenticationRepository
    @Inject
    constructor(
        private val authRetrofitService: AuthRetrofitService,
        private val jwtRepository: JwtRepository,
        @ApplicationContext private val context: Context,
    ) : AuthenticationRepository {
        override suspend fun refreshToken(jwt: Jwt): Result<Unit> {
            val response = authRetrofitService.refreshToken(jwt.refreshToken)
            val result = response.map { it.toJwt() }
            result.fold(
                onSuccess = {
                    jwtRepository.setJwt(it)
                    return Result.success(Unit)
                },
                onFailure = { return Result.failure(it) },
            )
        }

        override suspend fun userLoginWithAccessToken(accessToken: String): Result<Unit> {
            Log.d(TAG, "userLoginWithAccessToken: $accessToken")
            val result =
                authRetrofitService.userLogin(
                    LoginDTO(
                        accessToken,
                        "KAKAO",
                    ),
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
            Log.d(TAG, "companyLoginWithAccessToken: $accessToken")
            val result =
                authRetrofitService.companyLogin(
                    LoginDTO(
                        accessToken,
                        "KAKAO",
                    ),
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

        override suspend fun logout(): Result<Unit> {
            val jwt = jwtRepository.getJwt()
            val result =
                authRetrofitService.logout(
                    accessToken = "Bearer ${jwt.accessToken}",
                    refreshToken = "Bearer ${jwt.refreshToken}",
                )

            result.onSuccess {
                jwtRepository.clearJwt()
            }
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 폐기됨", error)
                } else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 폐기됨")
                }
            }

            return result
        }

        companion object {
            private const val TAG = "RemoteAuthenticationRepository"
        }
    }
