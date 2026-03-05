package com.ssavice.datastore.preferences

import androidx.datastore.core.Serializer
import com.ssavice.model.chat.ChattingServiceSummary
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class ServiceMetadataPreferences(
    val infos: Map<Long, ChattingServiceSummary> = emptyMap(),
)

object ServiceMetadataPreferencesSerializer : Serializer<ServiceMetadataPreferences> {
    override val defaultValue: ServiceMetadataPreferences = ServiceMetadataPreferences()

    override suspend fun readFrom(input: InputStream): ServiceMetadataPreferences =
        try {
            Json.decodeFromString(
                deserializer = ServiceMetadataPreferences.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (e: SerializationException) {
            defaultValue
        }

    override suspend fun writeTo(
        t: ServiceMetadataPreferences,
        output: OutputStream,
    ) {
        output.write(
            Json
                .encodeToString(
                    serializer = ServiceMetadataPreferences.serializer(),
                    value = t,
                ).encodeToByteArray(),
        )
    }
}
