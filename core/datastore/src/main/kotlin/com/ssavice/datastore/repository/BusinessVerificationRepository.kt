package com.ssavice.datastore.repository

import com.ssavice.model.auth.CompanyVerifyToken
import kotlinx.coroutines.flow.Flow

interface BusinessVerificationRepository {
    fun getToken(): Flow<CompanyVerifyToken>

    suspend fun setToken(token: CompanyVerifyToken)
}
