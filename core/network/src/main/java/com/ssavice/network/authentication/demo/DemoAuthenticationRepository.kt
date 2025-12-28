package com.ssavice.network.authentication.demo

import com.ssavice.network.authentication.AuthenticationRepository
import com.ssavice.network.model.JWT
import javax.inject.Inject

class DemoAuthenticationRepository
    @Inject
    constructor() : AuthenticationRepository {
        override fun refreshToken(jwt: JWT): JWT? = null
    }
