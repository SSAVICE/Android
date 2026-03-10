package com.ssavice.network.model.service

import com.ssavice.model.chat.ChattingServiceSummary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetChatServiceSummaryDTO(
    @SerialName("title")
    val serviceName: String,
    @SerialName("discountedPrice")
    val servicePrice: Int,
    @SerialName("thumbnailUrl")
    val serviceThumbnail: String,
    @SerialName("description")
    val serviceSeller: String,
) {
    fun toModel(id: Long) =
        ChattingServiceSummary(
            serviceId = id,
            serviceName = serviceName,
            servicePrice = servicePrice,
            serviceThumbnail = serviceThumbnail,
            serviceSeller = serviceSeller,
        )
}
