package com.ssavice.model.service

import com.ssavice.model.Date
import com.ssavice.model.enums.ServiceState

data class ServiceSummary(
    val name: String,
    val id: Long,
    val image: String,
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
    val state: ServiceState,
)
