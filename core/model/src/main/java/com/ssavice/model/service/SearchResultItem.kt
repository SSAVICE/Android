package com.ssavice.model.service

import com.ssavice.model.Date
import com.ssavice.model.Region

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
    val companyName: String,
    val companyId: Long,
    val region: Region,
    val booked: Boolean,
)

data class SearchResult(
    val items: List<SearchResultItem>,
    val hasNext: Boolean,
    val nextCursor: Long,
    val searchAfter: List<String> = emptyList(),
)
