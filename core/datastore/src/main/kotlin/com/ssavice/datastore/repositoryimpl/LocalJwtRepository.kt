package com.ssavice.datastore.repositoryimpl

import androidx.datastore.core.DataStore
import com.ssavice.datastore.preferences.UserAuthPreferences
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.model.TimeStamp
import com.ssavice.model.auth.Jwt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalJwtRepository @Inject constructor(
    private val dataStore: DataStore<UserAuthPreferences>
) : JwtRepository {
    override fun getJwt(): Flow<Jwt> =
        dataStore.data.map {
            it.jwt
        }

    override suspend fun setJwt(jwt: Jwt) {
        dataStore.updateData {
            it.copy(jwt = jwt)
        }
    }

    override suspend fun clearJwt() {
        dataStore.updateData {
            it.copy(jwt = Jwt("", "", TimeStamp(0L)))
        }
    }
}
