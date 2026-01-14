package com.ssavice.model

sealed interface ImageUploadProgress {
    object Waiting : ImageUploadProgress

    object Preprocessing : ImageUploadProgress

    data class Progress(
        val progressPercentile: Int,
    ) : ImageUploadProgress

    data class Done(
        val objectKey: String,
    ) : ImageUploadProgress

    data class Error(
        val throwable: Throwable,
    ) : ImageUploadProgress
}
