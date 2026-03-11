package com.ssavice.model.service

import com.ssavice.model.Date
import com.ssavice.model.RegionInfo
import com.ssavice.model.enums.Category

data class ServiceAddForm(
    val imageCount: Int = 0,
    val category: Category,
    val name: String,
    val description: String,
    val minimumRecruit: Int,
    val maximumRecruit: Int,
    val basePrice: Int,
    val discountRatio: Int,
    val discountedPrice: Int,
    val startDate: Date,
    val deadLine: Date,
    val endDate: Date,
    val tag: String,
    val region: RegionInfo,
    val imageObjectKeys: List<String>,
)
