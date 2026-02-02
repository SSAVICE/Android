package com.ssavice.model.seller

data class SellerProfileUpdateForm(
    val sellerName: String,
    val phoneNumber: String,
    val description: String,
    val detail: String
)
