package com.ssavice.datastore.repository

import com.ssavice.model.auth.Jwt

interface JwtRepository {
    suspend fun getJwt(): Jwt

    suspend fun setJwt(jwt: Jwt)

    suspend fun clearJwt()

    suspend fun consumeRefreshFlag(): Boolean

    suspend fun markRefreshNeeded()
}
