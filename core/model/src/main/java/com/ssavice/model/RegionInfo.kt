package com.ssavice.model

data class RegionInfo(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val detailAddress: String,
    val postCode: String,
) {
    companion object {
        val demo =
            RegionInfo(
                latitude = 10.1,
                longitude = 11.2,
                address = "대구 달서구 데모로 321",
                detailAddress = "데모아파트 101동 1005호",
                postCode = "12345",
            )
    }
}
