package com.ssavice.datastore.preferences

import androidx.datastore.core.Serializer
import com.ssavice.model.chat.ChattingUserInfo
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class UserInfoPreferences(
    val infos: Map<Long, ChattingUserInfo> = emptyMap(),
)

object UserInfoPreferencesSerializer : Serializer<UserInfoPreferences> {
    override val defaultValue: UserInfoPreferences = UserInfoPreferences()

    override suspend fun readFrom(input: InputStream): UserInfoPreferences =
        try {
            Json.decodeFromString(
                deserializer = UserInfoPreferences.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (e: SerializationException) {
            defaultValue
        }

    override suspend fun writeTo(
        t: UserInfoPreferences,
        output: OutputStream,
    ) {
        output.write(
            Json
                .encodeToString(
                    serializer = UserInfoPreferences.serializer(),
                    value = t,
                ).encodeToByteArray(),
        )
    }
}
