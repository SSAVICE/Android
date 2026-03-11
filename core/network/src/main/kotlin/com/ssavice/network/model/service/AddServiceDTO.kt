package com.ssavice.network.model.service

import com.ssavice.model.service.ServiceAddForm
import com.ssavice.network.model.RegionPostDTO
import kotlinx.serialization.Serializable

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
                    category = category.name,
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
                    region = RegionPostDTO.Companion.fromModel(region),
                    imageConfirms = objectKeys.map { ImageConfirmKeyDTO(it) },
                )
            }
    }
}

@Serializable
data class AddServiceResponseDTO(
    val serviceId: Long,
)

@Serializable
data class ImageConfirmKeyDTO(
    val objectKey: String,
)
