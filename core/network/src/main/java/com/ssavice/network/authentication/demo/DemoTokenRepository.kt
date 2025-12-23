package com.ssavice.network.authentication.demo

import com.ssavice.core.network.BuildConfig
import com.ssavice.network.authentication.TokenRepository
import com.ssavice.network.model.JWT
import javax.inject.Inject

class
DemoTokenRepository@Inject
    constructor() : TokenRepository {
        override fun getJWT(): JWT =
            JWT(
                System.currentTimeMillis() + 1_000_000L,
                "",
                BuildConfig.CONSUMER_TOKEN0,
            )

        override fun isTokenExpired(now: Long): Boolean = false

        override fun markRefreshNeeded() {
            TODO("Not yet implemented")
        }

        override fun consumeRefreshFlag(): Boolean = true

        override fun updateJwt(jwt: JWT) {
            TODO("Not yet implemented")
        }
    }
