package com.ssavice.user_home

import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.service.SearchQuery

data class UserHomeUiState(
    val categories: List<Category> = emptyList(),
    val selected: Int = 0,
    val defaultSearchQuery: SearchQuery =
        SearchQuery(
            query = "",
            region1 = "",
            region2 = "",
            category = Category.entries[0],
            searchRange = 1,
            minPrice = 0,
            maxPrice = Int.MAX_VALUE,
            sortBy = SortingOrder.POPULARITY,
            latitude = 0.0,
            longitude = 0.0,
        ),
    val addressState: RegionState = RegionState.Initial,
)

sealed interface RegionState {
    data class Showing(
        val address: String,
        val detailAddress: String,
        val longitude: Double,
        val latitude: Double,
        val regionCode: String,
        val postCode: String,
    ) : RegionState

    object Initial : RegionState

    object Loading : RegionState

    data class Error(
        val message: Throwable,
    ) : RegionState
}

sealed interface HomeUiEvent {
    object ShowAddressPicker : HomeUiEvent

    object ShowSearchScreen : HomeUiEvent
}
