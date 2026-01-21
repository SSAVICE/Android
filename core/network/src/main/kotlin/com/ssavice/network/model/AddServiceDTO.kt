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
    val region: RegionPostDTO,
    val imageConfirms: List<ImageConfirmKeyDTO>,
) {
    companion object {
        fun fromModel(
            serviceInfo: ServiceAddForm,
            objectKeys: List<String>,
        ): AddServiceDTO =
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
                    deadline = deadLine.toString(),
                    tag = tag,
                    startDate = startDate.toString(),
                    endDate = endDate.toString(),
                    discountRate = discountRatio.toLong(),
                    region = RegionPostDTO.fromModel(region),
                    imageConfirms = objectKeys.map { ImageConfirmKeyDTO(it) },
                )
            }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AddServiceResponseDTO(
    val serviceId: Long,
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ImageConfirmKeyDTO(
    val objectKey: String,
)
