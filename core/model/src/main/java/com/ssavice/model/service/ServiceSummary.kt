package com.ssavice.model.service

import com.ssavice.model.Date
import java.net.URL
import java.time.LocalDateTime

data class ServiceSummary(
    val name: String,
    val id: Long,
    val image: URL,
    val category: String,
    val minimumMember: Int,
    val currentMember: Int,
    val basePrice: Long,
    val discountRatio: Double,
    val discountedPrice: Long,
    val deadLine: Date,
    val startDate: Date,
    val endDate: Date,
    val serviceTag: String,
)
