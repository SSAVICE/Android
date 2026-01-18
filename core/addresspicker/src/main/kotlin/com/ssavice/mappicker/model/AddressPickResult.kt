package com.ssavice.mappicker.model

data class AddressPickResult(
    val address: String,
    val zipCode: String,
    val regionCode: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
