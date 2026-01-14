package com.ssavice.network

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink

class ProgressRequestBody(
    private val contentType: MediaType?,
    private val data: ByteArray,
    private val onProgress: (Float) -> Unit
) : RequestBody() {
    override fun contentType(): MediaType? = contentType
    override fun contentLength(): Long = data.size.toLong()

    override fun writeTo(sink: BufferedSink) {
        val bufferSize = 2048
        var uploaded = 0L

        val inputStream = data.inputStream()
        val buffer = ByteArray(bufferSize)

        try {
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                uploaded += read
                sink.write(buffer, 0, read)
                // 진행률 계산 (0.0 ~ 1.0)
                onProgress(uploaded.toFloat() / contentLength())
            }
        } finally {
            inputStream.close()
        }
    }
}
