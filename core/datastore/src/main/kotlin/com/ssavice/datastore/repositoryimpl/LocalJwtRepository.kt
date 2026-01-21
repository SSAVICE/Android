package com.ssavice.datastore.repositoryimpl

import androidx.datastore.core.DataStore
import com.ssavice.datastore.preferences.JwtPreferences
import com.ssavice.datastore.repository.JwtRepository
import com.ssavice.model.auth.Jwt
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

class LocalJwtRepository @Inject constructor(
    private val dataStore: DataStore<JwtPreferences>
) : JwtRepository {
    @OptIn(ExperimentalAtomicApi::class)
    private val refreshNeeded = AtomicBoolean(false)

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

    @OptIn(ExperimentalAtomicApi::class)
    override fun consumeRefreshFlag(): Boolean {
        return refreshNeeded.compareAndSet(expectedValue = true, newValue = false)
    }

    @OptIn(ExperimentalAtomicApi::class)
    override fun markRefreshNeeded() {
        refreshNeeded.store(true)
    }
}
