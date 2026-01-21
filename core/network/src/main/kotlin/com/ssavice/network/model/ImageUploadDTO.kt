package com.ssavice.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadDTO(
    val add: List<ContentTypeDTO>,
)

@Serializable
data class ContentTypeDTO(
    val contentType: String,
)

@Serializable
data class PresignedUrlDTO(
    val objectKey: String,
    val uploadUrl: String,
)

@Serializable
data class PresignedUrlResponseDTO(
    val list: List<PresignedUrlDTO>,
)
