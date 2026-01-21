package com.ssavice.network.model

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
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
    val regionCode: String
)
