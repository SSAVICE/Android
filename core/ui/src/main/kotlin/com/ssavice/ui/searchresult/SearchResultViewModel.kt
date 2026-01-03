package com.ssavice.ui.searchresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.model.Date
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.SortingOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel
    @Inject
    constructor(
        private val serviceRepository: ServiceRepository,
    ) : ViewModel() {
        private var searchJob: Job? = null

        private val _uiState =
            MutableStateFlow(
                SearchResultUiState(
                    items = listOf(),
                    status = SearchStatus.Loading,
                    hasNext = false,
                    nextId = 0,
                    searchQuery =
                        SearchQuery(
                            query = "",
                            region1 = "",
                            region2 = "",
                            searchRange = 0,
                            minPrice = 0,
                            maxPrice = 0,
                            sortBy = SortingOrder.POPULARITY,
                            category = "",
                            searchCount = 0,
                        ),
                ),
            )
        val uiState: StateFlow<SearchResultUiState> = _uiState

        fun newSearch(searchQuery: SearchQuery) {
            if (searchQuery.searchCount == 0) {
                return
            }

            _uiState.value =
                _uiState.value.copy(
                    searchQuery = searchQuery,
                    status = SearchStatus.Loading,
                    nextId = 0,
                    items = listOf(),
                )

            if (searchJob?.isActive == true) {
                searchJob?.cancel()
            }

            searchJob =
                viewModelScope.launch(Dispatchers.IO) {
                    val query = uiState.value.searchQuery
                    serviceRepository
                        .searchService(
                            query =
                                com.ssavice.model.service.SearchQuery(
                                    category = query.category,
                                    query = query.query,
                                    region1 = query.region1,
                                    region2 = query.region2,
                                    searchRange = query.searchRange,
                                    minPrice = query.minPrice,
                                    maxPrice = query.maxPrice,
                                    sortBy = query.sortBy,
                                ),
                            searchCount = SEARCH_COUNT,
                            startIndex = uiState.value.items.size,
                        ).fold(
                            onSuccess = {
                                updateSearchResult(it)
                            },
                            onFailure = {
                                onSearchFailure(it)
                            },
                        )
                }
        }

        private suspend fun search() {
            val query = uiState.value.searchQuery
            serviceRepository
                .searchService(
                    query =
                        com.ssavice.model.service.SearchQuery(
                            category = query.category,
                            query = query.query,
                            region1 = query.region1,
                            region2 = query.region2,
                            searchRange = query.searchRange,
                            minPrice = query.minPrice,
                            maxPrice = query.maxPrice,
                            sortBy = query.sortBy,
                        ),
                    nextId = uiState.value.nextId,
                    searchCount = SEARCH_COUNT,
                    startIndex = uiState.value.items.size,
                ).fold(
                    onSuccess = {
                        updateSearchResult(it)
                    },
                    onFailure = {
                        onSearchFailure(it)
                    },
                )
        }

        private fun updateSearchResult(searchResult: SearchResult) {
            val lastIndex = _uiState.value.items.size
            val newItems =
                _uiState.value.items.toMutableList().apply {
                    addAll(
                        searchResult.items.mapIndexed { i, item ->
                            val memberStatusText =
                                if (item.currentMember >= item.minimumMember) {
                                    "${item.currentMember}"
                                } else {
                                    "${item.currentMember}/${item.minimumMember}"
                                }
                            SearchResultItemUiState(
                                name = item.name,
                                tag = item.tag,
                                id = item.id,
                                index = i + lastIndex,
                                imageUrl = item.image,
                                companyName = item.companyName,
                                address = item.region2,
                                distance = "0.5km",
                                deadLine = getDeadlineMessage(item.deadLine),
                                discountedPrice = item.discountedPrice.toInt(),
                                basePrice = item.basePrice.toInt(),
                                discountRatio = item.discountRatio,
                                memberStatus = memberStatusText,
                            )
                        },
                    )
                }
            _uiState.value =
                _uiState.value.copy(
                    items = newItems,
                    status = SearchStatus.Shown,
                    hasNext = searchResult.hasNext,
                    nextId = searchResult.nextCursor,
                )
        }

        private fun getDeadlineMessage(deadline: Date): String {
            val deadline = deadline.toTimeStamp().timeInMillis
            val today = Date.now().toTimeStamp().timeInMillis

            val timeRemaining = deadline - today
            if (timeRemaining < 1000 * 60 * 60 * 24) {
                val hourRemaining = timeRemaining / (1000 * 60 * 60)

                return "${hourRemaining}시간 후 마감"
            } else {
                val dayRemaining = timeRemaining / (1000 * 60 * 60 * 24)
                return "${dayRemaining}일 후 마감"
            }
        }

        private fun onSearchFailure(exception: Throwable) {
        }

        fun loadMoreItems() {
            if (searchJob?.isActive == true) return
            if (!uiState.value.hasNext) return

            _uiState.value =
                _uiState.value.copy(
                    status = SearchStatus.Loading,
                )
            searchJob =
                viewModelScope.launch(Dispatchers.IO) {
                    search()
                }
        }

        fun clickItem() {
        }

        companion object {
            const val SEARCH_COUNT = 10
        }
    }
