package com.ssavice.network.model

import kotlinx.serialization.Serializable

@Serializable
data class GetAddressDTO(
    val address: String,
    val detailAddress: String,
    val gugun: String,
    val gugunCode: String,
    val latitude: Double,
    val longitude: Double,
    val postCode: String,
    val region: String,
    val regionCode: String,
)
