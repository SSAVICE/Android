package com.ssavice.datastore.repositoryimpl

import androidx.datastore.core.DataStore
import com.ssavice.datastore.preferences.JwtPreferences
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.model.auth.Jwt
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalJwtRepository @Inject constructor(
    private val dataStore: DataStore<JwtPreferences>
) : JwtRepository {
    override suspend fun getJwt(): Jwt =
        dataStore.data.map {
            it.jwt
        }.first()

    override suspend fun setJwt(jwt: Jwt) {
        dataStore.updateData {
            it.copy(jwt = jwt)
        }
    }

    override suspend fun clearJwt() {
        dataStore.updateData {
            it.copy(jwt = Jwt.EMPTY)
        }
    }

    override suspend fun consumeRefreshFlag(): Boolean {
        if(dataStore.data.first().needRefresh) {
            dataStore.updateData {
                it.copy(needRefresh = true)
            }
            return true
        }
        return false
    }

    override suspend fun markRefreshNeeded() {
        dataStore.updateData {
            it.copy(needRefresh = true)
        }
    }
}
