package com.ssavice.model.seller

import com.ssavice.model.RegionInfo

data class SellerProfileUpdateForm(
    val sellerName: String,
    val phoneNumber: String,
    val description: String,
    val detail: String,
    val region: RegionInfo,
)
