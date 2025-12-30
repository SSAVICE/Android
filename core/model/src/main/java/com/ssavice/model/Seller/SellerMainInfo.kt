package com.ssavice.model.Seller

import com.ssavice.model.Service.ServiceSummary

data class SellerMainInfo(
    val companyName: String,
    val ownerName: String,
    val phoneNumber: String,
    val businessNumber: String,
    val description: String,
    val services: List<ServiceSummary>,
)
