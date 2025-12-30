package com.ssavice.model.Service

import java.net.URL
import java.time.LocalDateTime

data class ServiceSummary(
    val name: String,
    val id: Long,
    val image: URL,
    val latitude: Double,
    val longitude: Double,
    val minimumMember: Int,
    val currentMember: Int,
    val basePrice: Long,
    val discountRatio: Double,
    val discountedPrice: Long,
    val deadLine: LocalDateTime,
    val serviceTag: List<String>,
)
