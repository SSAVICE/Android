package com.ssavice.model.service

import com.ssavice.model.Date
import com.ssavice.model.Region
import com.ssavice.model.enums.ServiceState

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
    val state: ServiceState
)

data class SearchResult(
    val items: List<SearchResultItem>,
    val hasNext: Boolean,
    val nextCursor: Long,
    val searchAfter: List<String> = emptyList(),
)
