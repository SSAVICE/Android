package com.ssavice.datastore.securestorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

abstract class SecureStorage<T> @Inject constructor(
    private val context: Context,
    private val serializer: KSerializer<T>,
    private val defaultValue: T,
    key: String
) {
    private val keysetHandle = AndroidKeysetManager.Builder()
        .withSharedPref(context, "master_keyset", "my_pref")
        .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
        .withMasterKeyUri("android-keystore://master_key")
        .build()
        .keysetHandle

    private val preferenceKey = stringPreferencesKey(DATA_KEY)
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = key)

    fun get(): Flow<T> {
        return context.dataStore.data.map {
            val data = it[preferenceKey] ?: return@map defaultValue
            deserializeGenericData(serializer, decrypt(data))
        }
    }

    suspend fun update(transform: suspend (T) -> T) {
        context.dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                val newData = preferences[preferenceKey]?.let { p ->
                    transform(deserializeGenericData(serializer, decrypt(p)))
                } ?: run {
                    transform(defaultValue)
                }
                preferences[preferenceKey] = encrypt(serializeGenericData(serializer, newData))
            }
        }
    }

    private fun <T> serializeGenericData(serializer: KSerializer<T>, data: T): String {
        return Json.encodeToString(serializer, data)
    }

    private fun deserializeGenericData(serializer: KSerializer<T>, data: String): T {
        return Json.decodeFromString(serializer, data)
    }

    private fun decrypt(encrypted: String): String {
        val aead = keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
        return aead.decrypt(encrypted.toByteArray(STRING_ENCODING), null).decodeToString()
    }

    private fun encrypt(data: String): String {
        val aead = keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
        return aead.encrypt(data.toByteArray(STRING_ENCODING), null).decodeToString()
    }

    companion object {
        private val STRING_ENCODING = Charsets.UTF_8
        private const val DATA_KEY = "data"
    }
}
