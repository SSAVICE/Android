package com.ssavice.model.seller

import com.ssavice.model.Region
import com.ssavice.model.Review
import com.ssavice.model.service.ServiceSummary

data class SellerDetail(
    val id: Long,
    val sellerName: String,
    val thumbnailUrl: String,
    val address: String,
    val detailAddress: String,
    val region: Region,
    val description: String,
    val detail: String,
    val phoneNumber: String,
    val imageUrls: List<String>,
    val serviceItems: List<ServiceSummary>,
    val reviewItems: List<Review>,
    val ownerName: String,
    val ownerPhoneNumber: String,
    val businessNumber: String
)
