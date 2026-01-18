package com.ssavice.datastore.preferences

import android.annotation.SuppressLint
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import com.ssavice.model.auth.CompanyVerifyToken
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BusinessVerificationPreferences(
    val token: CompanyVerifyToken
)

class BusinessVerificationPreferencesSerializer @Inject constructor(
    private val aead: Aead
) : Serializer<BusinessVerificationPreferences> {
    override val defaultValue: BusinessVerificationPreferences
        get() = BusinessVerificationPreferences(CompanyVerifyToken("", 0))

    override suspend fun readFrom(input: InputStream): BusinessVerificationPreferences =
        try {
            val encrypted = input.readBytes()
            val decrypted = aead.decrypt(encrypted, null)
            Json.decodeFromString<BusinessVerificationPreferences>(
                decrypted.decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }

    override suspend fun writeTo(t: BusinessVerificationPreferences, output: OutputStream) {
        val encrypted = aead.encrypt(Json.encodeToString(t)
            .encodeToByteArray(), null)
        output.write(encrypted)
    }
}
