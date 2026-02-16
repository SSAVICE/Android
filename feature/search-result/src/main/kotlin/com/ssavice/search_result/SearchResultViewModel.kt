package com.ssavice.search_result

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.service.SearchQuery
import com.ssavice.search_result.navigation.SearchResultRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
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
        val queryState: StateFlow<SearchQuery> =
            with(SearchResultRouteContract) {
                combine(
                    savedStateHandle.getStateFlow(SELECTED_CATEGORY, 0),
                    savedStateHandle.getStateFlow(SEARCH_RANGE, 0),
                    savedStateHandle.getStateFlow(START_PRICE, 0),
                    savedStateHandle.getStateFlow(END_PRICE, 10_000_000),
                    savedStateHandle.getStateFlow(SORT_BY, 0),
                    savedStateHandle.getStateFlow(QUERY, ""),
                ) { params ->
                    val category = params[0] as Int
                    val searchRange = params[1] as Int
                    val startPrice = params[2] as Int
                    val endPrice = params[3] as Int
                    val sortBy = params[4] as Int
                    val query = params[5] as String

                    val region = userRepository.getUserAddress().getOrNull()
                    val q =
                        SearchQuery(
                            query = query,
                            region1 = region?.regionInfo?.regionCode ?: "",
                            region2 = region?.regionInfo?.regionCode ?: "",
                            category = Category.entries.getOrElse(category, { Category.entries[0] }),
                            searchRange = searchRange,
                            minPrice = startPrice,
                            maxPrice = endPrice,
                            sortBy = SortingOrder.entries.getOrElse(sortBy, { SortingOrder.POPULARITY }),
                            latitude = region?.regionInfo?.latitude ?: 0.0,
                            longitude = region?.regionInfo?.longitude ?: 0.0
                        )
                    q
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue =
                        SearchQuery(
                            query = "",
                            region1 = "region1",
                            region2 = "region2",
                            category = Category.entries[0],
                            searchRange = 0,
                            minPrice = 0,
                            maxPrice = 10_000_000,
                            sortBy = SortingOrder.POPULARITY,
                            latitude = 0.0,
                            longitude = 0.0
                        ),
                )
            }

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
