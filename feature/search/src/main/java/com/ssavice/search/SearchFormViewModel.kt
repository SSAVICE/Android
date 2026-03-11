package com.ssavice.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SearchRange
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.enums.mapCategoryByName
import com.ssavice.model.enums.mapCategoryByValue
import com.ssavice.search.navigation.SearchFormRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchFormViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SearchFormUiState> by lazy {
        val categories = Category.entries.filter { it.showInUser }
        val categorySelectionName =
            savedStateHandle.get<String>(SearchFormRouteContract.SELECTED_CATEGORY)
        MutableStateFlow(
            SearchFormUiState(
                form =
                    SearchForm(
                        query = savedStateHandle.get<String>(SearchFormRouteContract.QUERY) ?: "",
                        categories = categories,
                        selectedCategory =
                            Category.mapCategoryByName(categorySelectionName ?: "ALL"),
                        searchRange =
                            SearchRange.entries.getOrElse(
                                savedStateHandle.get<Int>(
                                    SearchFormRouteContract.SEARCH_RANGE
                                ) ?: 0
                            ) { SearchRange.entries[0] },
                        priceRange =
                            savedStateHandle
                                .get<Int>(SearchFormRouteContract.START_PRICE)
                                ?.let { start ->
                                    savedStateHandle
                                        .get<Int>(SearchFormRouteContract.END_PRICE)
                                        ?.let { end ->
                                            start..end
                                        } ?: run { start..10_000_000 }
                                } ?: run { 0..10_000_000 },
                        sortBy =
                            (
                                    SortingOrder.entries.getOrNull(
                                        savedStateHandle.get<Int>(
                                            SearchFormRouteContract.SORT_BY,
                                        ) ?: -1,
                                    )
                                    ) ?: SortingOrder.entries[0],
                        onSaleOnly =
                            savedStateHandle.get<Boolean>(SearchFormRouteContract.ON_SALE_ONLY)
                                ?: false
                    ),
                region1String =
                    savedStateHandle.get<String>(SearchFormRouteContract.REGION1) ?: "",
                region2String =
                    savedStateHandle.get<String>(SearchFormRouteContract.REGION2) ?: "",
            ),
        )
    }

    val uiState = _uiState

    fun onQuery(query: String) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value.form.copy(
                        query = query,
                    ),
            )
    }

    fun onCategorySelect(category: Category) {
        _uiState.update {
            _uiState.value.copy(
                form =
                    _uiState.value.form.copy(
                        selectedCategory = category,
                    ),
            )
        }
    }

    fun onSearchRangeSelect(range: SearchRange) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value.form.copy(
                        searchRange = range,
                    ),
            )
    }

    fun onPriceRangeChange(range: IntRange) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value.form.copy(
                        priceRange = range,
                    ),
            )
    }

    fun onSortByChange(order: SortingOrder) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value.form.copy(
                        sortBy = order,
                    ),
            )
    }

    fun initiateRegion() {
        if (_uiState.value.region1String.isEmpty() || _uiState.value.region2String.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                userInfoRepository.getUserAddress().onSuccess {
                    _uiState.value =
                        _uiState.value.copy(
                            region1String = it.region1,
                            region2String = it.region2,
                        )
                }
            }
        }
    }

    fun onSaleOnlyChanged(onSale: Boolean) {
        _uiState.update {
            it.copy(
                form =
                    it.form.copy(
                        onSaleOnly = onSale,
                    ),
            )
        }
    }
}
