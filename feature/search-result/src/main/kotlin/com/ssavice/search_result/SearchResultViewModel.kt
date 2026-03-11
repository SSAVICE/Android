package com.ssavice.search_result

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SearchRange
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.service.SearchQuery
import com.ssavice.search_result.navigation.SearchResultRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserInfoRepository,
) : ViewModel() {
    val queryState = MutableStateFlow(
        with(SearchResultRouteContract) {
            val category = savedStateHandle.get<Int>(SELECTED_CATEGORY)
            val searchRange = savedStateHandle.get<Int>(SEARCH_RANGE)
            val startPrice = savedStateHandle.get<Int>(START_PRICE)
            val endPrice = savedStateHandle.get<Int>(END_PRICE)
            val sortBy = savedStateHandle.get<Int>(SORT_BY)
            val query = savedStateHandle.get<String>(QUERY)
            val onSaleOnly = savedStateHandle.get<Boolean>(ON_SALE_ONLY)
            val region1 = savedStateHandle.get<String>(REGION1)
            val region2 = savedStateHandle.get<String>(REGION2)
            val longitude = savedStateHandle.get<Double>(LONGITUDE)
            val latitude = savedStateHandle.get<Double>(LATITUDE)

            val q =
                SearchQuery(
                    query = query ?: "",
                    region1 = region1 ?: "",
                    region2 = region2 ?: "",
                    category = Category.entries.getOrElse(
                        category ?: 0
                    ) { Category.entries[0] },
                    searchRange = SearchRange.entries.getOrElse(
                        searchRange ?: 0
                    ) { SearchRange.entries[0] },
                    minPrice = startPrice ?: 0,
                    maxPrice = endPrice ?: 100_000_000,
                    sortBy = SortingOrder.entries.getOrElse(
                        sortBy ?: 0
                    ) { SortingOrder.entries[0] },
                    latitude = latitude ?: 0.0,
                    longitude = longitude ?: 0.0,
                    onSaleOnly = onSaleOnly ?: false,
                )
            q
        }
    )

    val uiState: StateFlow<SearchResultUiState> =
        queryState
            .transform {
                emit(SearchResultUiState(searchQuery = it))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchResultUiState(searchQuery = queryState.value),
            )

    override fun onCleared() {
        Log.d("KSC", "Search Result Cleared")
    }
}
