package com.ssavice.seller_main

import java.net.URL

data class SellerMainUiState (
    val items: List<SellerItemUiState>
)

data class SellerItemUiState (
    val id: Long,
    val title: String,
    val category: String,
    val meta: String,
    val priceText: String,
    val isRecruiting: Boolean,
    val imageUrl: URL
)
