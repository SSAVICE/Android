package com.ssavice.datastore.preferences

import com.google.crypto.tink.Aead
import com.ssavice.model.auth.Jwt
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class JwtPreferences(
    val jwt: Jwt
)

data class JwtPreferencesSerializer @Inject constructor(private val aead: Aead) :
    EncryptedJsonSerializer<JwtPreferences>(aead) {
    override val defaultValue: JwtPreferences
        get() = JwtPreferences(
            jwt = Jwt.EMPTY
        )

    override fun decode(value: String): JwtPreferences =
        Json.decodeFromString<JwtPreferences>(
            value,
        )

    override fun encode(value: JwtPreferences): String =
        Json.encodeToString(value)
}
