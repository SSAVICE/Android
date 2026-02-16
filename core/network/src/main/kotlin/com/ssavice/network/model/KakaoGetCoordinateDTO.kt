package com.ssavice.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class KakaoGetCoordinateDTO(
    val documents: List<KakaoGetCoordinateDocumentDTO>,
)

@Serializable
@JsonIgnoreUnknownKeys
data class KakaoGetCoordinateDocumentDTO(
    val x: Double,
    val y: Double,
)
