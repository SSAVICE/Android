package com.ssavice.model

data class ServiceInfo(
    val imageCount: Int = 0,
    val category: String,
    val name: String,
    val description: String,
    val minimumRecruit: Int,
    val maximumRecruit: Int,
    val basePrice: Int,
    val discountRatio: Int,
    val discountedPrice: Int,
    val startDate: Date,
    val endDate: Date,
    val tags: List<String>,
)
