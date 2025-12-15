package com.ssavice.network.authentication.demo

import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.model.JWT

class DemoAuthenticationRepository : AuthenticationRepository {
    override fun refreshToken(jwt: JWT): JWT? {
        TODO("Not yet implemented")
    }
}
