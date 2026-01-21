package com.ssavice.datastore.repository

import com.ssavice.model.auth.Jwt
import kotlinx.coroutines.flow.Flow

interface JwtRepository {
    fun getJwt(): Flow<Jwt>

    suspend fun setJwt(jwt: Jwt)

    suspend fun clearJwt()
}
