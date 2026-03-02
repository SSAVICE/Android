package com.ssavice.model.seller

import com.ssavice.model.Date
import com.ssavice.model.RegionInfo

data class SellerRegisterForm(
    val companyName: String,
    val businessOwnerName: String,
    val phoneNumber: String,
    val businessNumber: String,
    val accountDepositor: String,
    val accountNumber: String,
    val description: String,
    val detail: String,
    val businessName: String,
    val companyOpenDate: Date,
    val region: RegionInfo,
)
