package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.service.ServiceAddForm
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AddServiceDTO(
    val imageCnt: Long,
    val category: String,
    val title: String,
    val description: String,
    val minimumMember: Long,
    val maximumMember: Long,
    val basePrice: Long,
    val discountRate: Long,
    val discountedPrice: Long,
    val deadline: String,
    val tag: String,
    val startDate: String,
    val endDate: String,
    val longitude: Double,
    val latitude: Double,
    val postCode: String,
    val address: String,
    val detailAddress: String,
) {
    companion object {
        fun fromModel(serviceInfo: ServiceAddForm): AddServiceDTO =
            with(serviceInfo) {
                AddServiceDTO(
                    imageCnt = imageCount.toLong(),
                    category = category,
                    title = name,
                    description = description,
                    minimumMember = minimumRecruit.toLong(),
                    maximumMember = maximumRecruit.toLong(),
                    basePrice = basePrice.toLong(),
                    discountedPrice = discountedPrice.toLong(),
                    deadline = endDate.toString(),
                    tag = tag,
                    startDate = startDate.toString(),
                    endDate = endDate.toString(),
                    longitude = region.longitude, // TODO
                    latitude = region.latitude,
                    postCode = region.postCode,
                    address = region.address,
                    detailAddress = region.detailAddress,
                    discountRate = discountRatio.toLong(),
                )
            }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AddServiceResponseDTO(
    val serviceId: Long,
)
