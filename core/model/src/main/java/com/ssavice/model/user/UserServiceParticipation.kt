package com.ssavice.model.user

import com.ssavice.model.Date
import com.ssavice.model.service.ServiceState

data class UserServiceParticipationItem(
    val id: Long,
    val thumbnail: String,
    val category: String,
    val price: Int,
    val name: String,
    val sellerName: String,
    val startDate: Date,
    val endDate: Date,
    val state: ServiceState,
    val isReviewed: Boolean,
)

data class UserServiceParticipation(
    val items: List<UserServiceParticipationItem>,
    val currentPage: Long,
    val searchCount: Int,
    val hasNext: Boolean,
)
