package com.ssavice.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ImageUploadDTO(
    val add: List<ContentTypeDTO>,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ContentTypeDTO(
    val contentType: String,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PresignedUrlDTO(
    val objectKey: String,
    val uploadUrl: String,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class PresignedUrlResponseDTO(
    val list: List<PresignedUrlDTO>,
)
