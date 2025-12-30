package com.ssavice.model.seller

import com.ssavice.model.RegionInfo
import com.ssavice.model.service.ServiceSummary

data class SellerMainInfo(
    val companyName: String,
    val phoneNumber: String,
    val businessNumber: String,
    val description: String,
    val services: List<ServiceSummary>,
    val region: RegionInfo
)
