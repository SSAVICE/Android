package com.ssavice.datastore.preferences

import com.google.crypto.tink.Aead
import com.ssavice.model.auth.CompanyVerifyToken
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

@Serializable
data class BusinessVerificationPreferences(
    val token: CompanyVerifyToken,
)

class BusinessVerificationPreferencesSerializer
    @Inject
    constructor(
        aead: Aead,
    ) : EncryptedJsonSerializer<BusinessVerificationPreferences>(aead) {
        override val defaultValue: BusinessVerificationPreferences
            get() = BusinessVerificationPreferences(CompanyVerifyToken("", 0))

        override fun decode(value: String): BusinessVerificationPreferences =
            Json.decodeFromString<BusinessVerificationPreferences>(
                value,
            )

        override fun encode(value: BusinessVerificationPreferences): String = Json.encodeToString(value)
    }
