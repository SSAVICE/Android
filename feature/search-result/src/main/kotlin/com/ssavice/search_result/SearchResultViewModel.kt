package com.ssavice.search_result

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.model.Category
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SortingOrder
import com.ssavice.search_result.navigation.SearchResultRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {


    val queryState: StateFlow<SearchQuery> = with(SearchResultRouteContract) {
        combine(
            savedStateHandle.getStateFlow(SELECTED_CATEGORY, 0),
            savedStateHandle.getStateFlow(SEARCH_RANGE, 0),
            savedStateHandle.getStateFlow(START_PRICE, 0),
            savedStateHandle.getStateFlow(END_PRICE, 10_000_000),
            savedStateHandle.getStateFlow(SORT_BY, 0),
        ) {
            category, searchRange, startPrice, endPrice, sortBy ->
            listOf(category, searchRange, startPrice, endPrice, sortBy)
        }
            .combine(savedStateHandle.getStateFlow(QUERY, "")) {
                o1, query ->
                val q =
                    SearchQuery(
                        query = query,
                        region1 = "region1",
                        region2 = "region2",
                        category = Category.entries.getOrElse(o1[0], { Category.entries[0] }),
                        searchRange = o1[1],
                        minPrice = o1[2],
                        maxPrice = o1[3],
                        sortBy = SortingOrder.entries.getOrElse(o1[4], {SortingOrder.POPULARITY}),
                    )
                Log.d("query", q.toString())
                q
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SearchQuery(
                    query = "",
                    region1 = "region1",
                    region2 = "region2",
                    category = Category.entries[0],
                    searchRange = 0,
                    minPrice = 0,
                    maxPrice = 10_000_000,
                    sortBy = SortingOrder.POPULARITY,
                )
            )
    }

    val uiState: StateFlow<SearchResultUiState> = queryState.transform {
        emit(SearchResultUiState(searchQuery = it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchResultUiState(searchQuery = queryState.value)
    )
}
