package com.ssavice.seller_main

import com.ssavice.model.enums.ServiceState

sealed interface SellerMainUiState {
    data object Loading : SellerMainUiState

    data class Error(
        val message: String,
    ) : SellerMainUiState

    data class Shown(
        val items: List<SellerItemUiState>,
    ) : SellerMainUiState
}

data class SellerItemUiState(
    val id: Long,
    val title: String,
    val category: String,
    val meta: String,
    val priceText: String,
    val isRecruiting: Boolean,
    val imageUrl: String,
    val state: ServiceState,
)
