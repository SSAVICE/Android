package com.ssavice.model.service

import com.ssavice.model.Date
import com.ssavice.model.RegionInfo

data class ServiceDetail(
    val imageUrl: String,
    val id: Long,
    val category: String,
    val companyId: Long,
    val name: String,
    val description: String,
    val tag: String,
    val basePrice: Int,
    val discountRatio: Int,
    val discountedPrice: Int,
    val minimumMember: Int,
    val maximumMember: Int,
    val currentMember: Int,
    val deadLine: Date,
    val startDate: Date,
    val endDate: Date,
    val liked: Boolean,
    val status: String,
    val createdAt: Date,
    val regionInfo: RegionInfo
)
