package com.ssavice.datastore.preferences

import android.annotation.SuppressLint
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.ssavice.model.auth.CompanyVerifyToken
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BusinessVerificationPreferences(
    val token: CompanyVerifyToken
)

object BusinessVerificationPreferencesSerializer : Serializer<BusinessVerificationPreferences> {
    override val defaultValue: BusinessVerificationPreferences
        get() = BusinessVerificationPreferences(CompanyVerifyToken("", 0))

    override suspend fun readFrom(input: InputStream): BusinessVerificationPreferences =
        try {
            Json.decodeFromString<BusinessVerificationPreferences>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }

    override suspend fun writeTo(t: BusinessVerificationPreferences, output: OutputStream) {
        output.write(
            Json.encodeToString(t)
                .encodeToByteArray()
        )
    }
}
