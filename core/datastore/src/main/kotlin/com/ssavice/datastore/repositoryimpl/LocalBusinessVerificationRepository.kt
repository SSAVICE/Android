package com.ssavice.datastore.repositoryimpl

import android.content.Context
import com.ssavice.datastore.preferences.BusinessVerificationPreferences
import com.ssavice.datastore.repository.BusinessVerificationRepository
import com.ssavice.datastore.securestorage.SecureStorage
import com.ssavice.model.auth.CompanyVerifyToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalBusinessVerificationRepository @Inject constructor(
    @ApplicationContext private val context: Context
): BusinessVerificationRepository, SecureStorage<BusinessVerificationPreferences>(
    context = context,
    serializer = BusinessVerificationPreferences.serializer(),
    defaultValue = BusinessVerificationPreferences(CompanyVerifyToken("", 0)),
    key = "business_verification") {
    override fun getToken(): Flow<CompanyVerifyToken> =
        get().map { it.token }


    override suspend fun setToken(token: CompanyVerifyToken) {
        update {
            it.copy(token = token)
        }
    }
}
