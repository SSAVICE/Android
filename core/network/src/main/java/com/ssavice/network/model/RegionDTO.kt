package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.Region
import com.ssavice.model.RegionInfo
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class RegionDTO(
    val gugun: String?,
    val region: String?,
    val latitude: Double?,
    val longitude: Double?,
) {
    fun toModel(): Region =
        Region(
            region1 = gugun ?: "",
            region2 = region ?: "",
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0,
        )

    companion object {
        fun fromModel(model: Region): RegionDTO =
            RegionDTO(
                gugun = model.region1,
                region = model.region2,
                latitude = model.latitude,
                longitude = model.longitude,
            )
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class RegionPostDTO(
    val regionCode: String,
    val postCode: String,
    val address: String,
    val detailAddress: String,
    val latitude: Double,
    val longitude: Double,
) {
    fun toModel(): RegionInfo =
        RegionInfo(
            regionCode = regionCode,
            postCode = postCode,
            address = address,
            detailAddress = detailAddress,
            latitude = latitude,
            longitude = longitude,
        )

    companion object {
        fun fromModel(model: RegionInfo): RegionPostDTO =
            RegionPostDTO(
                regionCode = model.regionCode,
                postCode = model.postCode,
                address = model.address,
                detailAddress = model.detailAddress,
                latitude = model.latitude,
                longitude = model.longitude,
            )
    }
}
