package com.ssavice.datastore.preferences

import com.google.crypto.tink.Aead
import com.ssavice.model.TimeStamp
import com.ssavice.model.auth.Jwt
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class UserAuthPreferences(
    val jwt: Jwt,
)

data class UserAuthPreferencesSerializer @Inject constructor(private val aead: Aead) :
    EncryptedJsonSerializer<UserAuthPreferences>(aead) {
    override val defaultValue: UserAuthPreferences
        get() = UserAuthPreferences(
            jwt = Jwt(
                accessToken = "",
                refreshToken = "",
                accessTokenExpiresAt = TimeStamp(0L)
            )
        )

    override fun decode(value: String): UserAuthPreferences =
        Json.decodeFromString<UserAuthPreferences>(
            value,
        )

    override fun encode(value: UserAuthPreferences): String =
        Json.encodeToString(value)
}
