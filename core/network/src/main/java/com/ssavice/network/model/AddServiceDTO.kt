package com.ssavice.network.model

import android.annotation.SuppressLint
import com.ssavice.model.ServiceInfo
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
    val discountedPrice: Long,
    val deadline: String,
    val tag: List<String>,
    val startDate: String,
    val endDate: String,
    val longitude: Double,
    val latitude: Double,
    val postCode: String,
    val address: String,
    val detailAddress: String,
) {
    companion object {
        fun fromModel(serviceInfo: ServiceInfo): AddServiceDTO =
            AddServiceDTO(
                imageCnt = serviceInfo.imageCount.toLong(),
                category = serviceInfo.category,
                title = serviceInfo.name,
                description = serviceInfo.description,
                minimumMember = serviceInfo.minimumRecruit.toLong(),
                maximumMember = serviceInfo.maximumRecruit.toLong(),
                basePrice = serviceInfo.basePrice.toLong(),
                discountedPrice = serviceInfo.discountedPrice.toLong(),
                deadline = serviceInfo.endDate.toString(),
                tag = serviceInfo.tags,
                startDate = serviceInfo.startDate.toString(),
                endDate = serviceInfo.endDate.toString(),
                longitude = 0.0, // TODO
                latitude = 0.0,
                postCode = "12345",
                address = "54321",
                detailAddress = "ABCDE",
            )
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AddServiceResponseDTO(
    val serviceId: Long,
)
