package com.ssavice.datastore.repositoryimpl

import androidx.datastore.core.DataStore
import com.ssavice.datastore.preferences.BusinessVerificationPreferences
import com.ssavice.datastore.repository.BusinessVerificationRepository
import com.ssavice.model.auth.CompanyVerifyToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalBusinessVerificationRepository
    @Inject
    constructor(
        private val dataStore: DataStore<BusinessVerificationPreferences>,
    ) : BusinessVerificationRepository {
        override fun getToken(): Flow<CompanyVerifyToken> =
            dataStore.data.map {
                it.token
            }

        override suspend fun setToken(token: CompanyVerifyToken) {
            dataStore.updateData {
                it.copy(token = token)
            }
        }

        override suspend fun clearToken() {
            dataStore.updateData {
                it.copy(token = CompanyVerifyToken("", 0))
            }
        }
    }
