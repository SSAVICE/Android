package com.ssavice.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ssavice.model.Category
import com.ssavice.model.service.SortingOrder
import com.ssavice.search.navigation.SearchFormRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SearchFormUiState(
            form = SearchForm(
                query = savedStateHandle.get<String>(SearchFormRouteContract.QUERY) ?: "",
                categories = Category.entries.map { it.name },
                selectedCategory = savedStateHandle.get<Int>(SearchFormRouteContract.SELECTED_CATEGORY)
                    ?: 0,
                searchRange = savedStateHandle.get<Int>(SearchFormRouteContract.SEARCH_RANGE) ?: 0,
                priceRange = savedStateHandle.get<Int>(SearchFormRouteContract.START_PRICE)
                    ?.let { start ->
                        savedStateHandle.get<Int>(SearchFormRouteContract.END_PRICE)?.let { end ->
                            start..end
                        } ?: run { start..10_000_000 }
                    } ?: run { 0..10_000_000 },
                sortBy = (SortingOrder.entries.getOrNull(
                    savedStateHandle.get<Int>(
                        SearchFormRouteContract.SORT_BY
                    ) ?: -1
                )) ?: SortingOrder.POPULARITY
            )
        )
    )

    val uiState = _uiState

    fun onQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(
                query = query
            )
        )
    }

    fun onCategorySelect(index: Int) {
        if (index in 0 until Category.entries.size) {
            _uiState.value = _uiState.value.copy(
                form = _uiState.value.form.copy(
                    selectedCategory = index
                )
            )
        }
    }

    fun onSearchRangeSelect(index: Int) {
        if (index in 0..1) {
            _uiState.value = _uiState.value.copy(
                form = _uiState.value.form.copy(
                    searchRange = index
                )
            )
        }
    }

    fun onPriceRangeChange(range: IntRange) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(
                priceRange = range
            )
        )
    }

    fun onSortByChange(index: Int) {
        if (index in 0 until SortingOrder.entries.size) {
            _uiState.value = _uiState.value.copy(
                form = _uiState.value.form.copy(
                    sortBy = SortingOrder.entries[index]
                )
            )
        }
    }

}
