package com.ssavice.datastore.preferences

import android.annotation.SuppressLint
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import com.ssavice.model.auth.CompanyVerifyToken
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class BusinessVerificationPreferences(
    val token: CompanyVerifyToken,
)

class BusinessVerificationPreferencesSerializer
@Inject constructor(aead: Aead) :
    EncryptedJsonSerializer<BusinessVerificationPreferences>(aead) {
    override val defaultValue: BusinessVerificationPreferences
        get() = BusinessVerificationPreferences(CompanyVerifyToken("", 0))

    override fun decode(value: String): BusinessVerificationPreferences =
        Json.decodeFromString<BusinessVerificationPreferences>(
            value,
        )

    override fun encode(value: BusinessVerificationPreferences): String =
        Json.encodeToString(value)
}
