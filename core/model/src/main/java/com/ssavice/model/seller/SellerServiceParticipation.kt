package com.ssavice.model.seller

import com.ssavice.model.Date
import com.ssavice.model.enums.ServiceState

data class SellerServiceParticipationItem(
    val id: Long,
    val thumbnail: String,
    val category: String,
    val price: Int,
    val name: String,
    val sellerName: String,
    val sellerId: Long,
    val startDate: Date,
    val endDate: Date,
    val state: ServiceState,
    val isReviewed: Boolean,
    val currentMemberCount: Int,
    val minimumMemberCount: Int,
    val maximumMemberCount: Int,
)

data class SellerServiceParticipation(
    val items: List<SellerServiceParticipationItem>,
    val currentPage: Long,
    val searchCount: Int,
    val hasNext: Boolean,
)
