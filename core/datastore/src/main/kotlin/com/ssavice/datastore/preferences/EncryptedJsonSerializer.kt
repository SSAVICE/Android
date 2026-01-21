package com.ssavice.datastore.preferences

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import kotlinx.serialization.SerializationException
import java.io.InputStream
import java.io.OutputStream

abstract class EncryptedJsonSerializer<T>(
    private val aead: Aead,
) : Serializer<T> {
    abstract fun encode(value: T): String

    abstract fun decode(value: String): T

    override suspend fun readFrom(input: InputStream): T =
        try {
            val encrypted = input.readBytes()
            val decrypted = aead.decrypt(encrypted, null)
            decode(decrypted.decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }

    override suspend fun writeTo(
        t: T,
        output: OutputStream,
    ) {
        val encrypted =
            aead.encrypt(
                encode(t).encodeToByteArray(),
                null,
            )
        output.write(encrypted)
    }
}
