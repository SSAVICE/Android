package com.ssavice.seller_my_service

import com.ssavice.model.enums.ServiceState

data class SellerMyServiceUiState(
    val services: List<SellerMyServiceItemUiState> = listOf(),
    val myServiceScreenStatus: MyServiceState = MyServiceState.Loading,
    val hasNext: Boolean = false,
    val nextPage: Int = 0,
    val searchTypeSelection: Int = 0,
    val searchingState: List<ServiceState> = ServiceState.entries.filter { it.visibleAsOption },
)

data class SellerMyServiceItemUiState(
    val index: Int,
    val id: Long,
    val title: String,
    val price: String,
    val thumbnailUrl: String,
    val sellerName: String,
    val duration: String,
    val cancellable: Boolean,
    val sellerId: Long,
    val state: ServiceState,
    val memberStatus: String,
)

sealed interface MyServiceState {
    object Initial : MyServiceState

    object Loading : MyServiceState

    object Loaded : MyServiceState

    data class Error(
        val message: Throwable,
    ) : MyServiceState
}
