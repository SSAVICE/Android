package com.ssavice.data.repository

interface ImageRepository {
    fun uploadImage(
        image: ByteArray,
        storageUrl: String,
    ): Result<Unit>
}
