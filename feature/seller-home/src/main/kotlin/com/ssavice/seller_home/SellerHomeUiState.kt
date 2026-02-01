package com.ssavice.seller_home

import com.ssavice.model.enums.ServiceState

sealed interface SellerHomeUiState {
    data object Loading : SellerHomeUiState

    data class Error(
        val message: String,
    ) : SellerHomeUiState

    data class Shown(
        val items: List<SellerItemUiState>,
    ) : SellerHomeUiState
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
