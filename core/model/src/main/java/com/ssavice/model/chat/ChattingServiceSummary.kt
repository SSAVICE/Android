package com.ssavice.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChattingServiceSummary(
    val serviceId: Long,
    val serviceName: String,
    val servicePrice: Int,
    val serviceThumbnail: String,
    val serviceSeller: String,
    val lastUpdate: Long = System.currentTimeMillis()
) {
    fun needRefresh(): Boolean {
        return (System.currentTimeMillis() - lastUpdate) > SERVICE_SUMMARY_REFRESH_CYCLE
    }

    companion object {
        const val SERVICE_SUMMARY_REFRESH_CYCLE = 1000 * 3600 * 6
    }
}
