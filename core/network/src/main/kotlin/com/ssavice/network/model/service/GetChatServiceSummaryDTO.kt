package com.ssavice.network.model.service

import com.ssavice.model.chat.ChattingServiceSummary
import kotlinx.serialization.Serializable

@Serializable
data class GetChatServiceSummaryDTO(
    val serviceName: String,
    val servicePrice: Int,
    val serviceThumbnail: String,
    val serviceSeller: String
) {
    fun toModel(id: Long) = ChattingServiceSummary(
        serviceId = id,
        serviceName = serviceName,
        servicePrice = servicePrice,
        serviceThumbnail = serviceThumbnail,
        serviceSeller = serviceSeller
    )
}
