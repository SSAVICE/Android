package com.ssavice.model.service

import com.ssavice.model.Date
import com.ssavice.model.Region

data class WishList(
    val items: List<UserWishListItem>,
    val currentPage: Long,
    val searchCount: Int,
    val hasNext: Boolean,
)

data class UserWishListItem(
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
)
