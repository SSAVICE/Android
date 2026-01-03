package com.ssavice.model.service

import com.ssavice.model.Date

data class SearchResultItem(
    val name: String,
    val tag: String,
    val id: Long,
    val image: String,
    val category: String,
    val minimumMember: Int,
    val currentMember: Int,
    val basePrice: Long,
    val discountRatio: Int,
    val discountedPrice: Long,
    val deadLine: Date,
    val latitude: Double,
    val longitude: Double,
    val region1: String,
    val region2: String,
    val companyName: String,
    val companyId: Long,
)

data class SearchResult(
    val items: List<SearchResultItem>,
    val hasNext: Boolean,
    val nextCursor: Long,
)
