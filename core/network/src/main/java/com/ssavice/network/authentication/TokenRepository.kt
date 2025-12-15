package com.ssavice.network.authentication

import com.ssavice.network.model.JWT

interface TokenRepository {
    fun getJWT(): JWT
    fun isTokenExpired(now: Long = System.currentTimeMillis()): Boolean
    fun markRefreshNeeded()
    fun consumeRefreshFlag(): Boolean
    fun updateJwt(jwt: JWT)
}
