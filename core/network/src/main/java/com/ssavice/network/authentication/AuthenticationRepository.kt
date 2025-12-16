package com.ssavice.network.authentication

import com.ssavice.network.model.JWT

interface AuthenticationRepository {
    fun refreshToken(jwt: JWT): JWT?
}
