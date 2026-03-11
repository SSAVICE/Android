package com.ssavice.ui.searchresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.common.getDeadlineMessageFromTimestamp
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.Date
import com.ssavice.model.RegionDetail
import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SearchRange
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
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
    private val userInfoRepository: UserInfoRepository,
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
                        searchRange = SearchRange.entries[0],
                        minPrice = 0,
                        maxPrice = 0,
                        sortBy = SortingOrder.entries[0],
                        category = Category.entries[0],
                        latitude = 0.0,
                        longitude = 0.0,
                        onSaleOnly = true,
                    ),
            ),
        )
    val uiState: StateFlow<SearchResultUiState> = _uiState

    fun newSearch(searchQuery: SearchQuery) {
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
                val address =
                    userInfoRepository.getUserAddress().getOrElse {
                        return@launch
                    }

                val query = getNewSearchQuery(uiState.value.searchQuery, address)
                getNewSearchResult(query, V2).fold(
                        onSuccess = {
                            updateSearchResult(it)
                        },
                        onFailure = {
                            onSearchFailure(it)
                        },
                    )
            }
    }

    private fun getNewSearchQuery(baseQuery: SearchQuery, region: RegionDetail): SearchQuery =
        baseQuery.copy(
            region1 = region.region1,
            region2 = region.region2,
            latitude = region.regionInfo.latitude,
            longitude = region.regionInfo.longitude,
        )

    private suspend fun getNewSearchResult(query: SearchQuery, v2: Boolean): Result<SearchResult> =
        if (v2) newSearchV2(query) else newSearchV1(query)

    private suspend fun newSearchV1(query: SearchQuery): Result<SearchResult> =
        serviceRepository
            .searchService(
                query = query,
                searchCount = SEARCH_COUNT,
                startIndex = uiState.value.items.size,
            )

    private suspend fun newSearchV2(query: SearchQuery): Result<SearchResult> =
        serviceRepository
            .searchServiceV2(
                query = query,
                searchCount = SEARCH_COUNT,
                startIndex = uiState.value.items.size,
            )


    private suspend fun search() {
        val address =
            userInfoRepository.getUserAddress().getOrElse {
                return
            }
        val query = getNewSearchQuery(uiState.value.searchQuery, address)

        getSearchResult(query, V2).fold(
            onSuccess = {
                updateSearchResult(it)
            },
            onFailure = {
                onSearchFailure(it)
            },
        )
    }

    private suspend fun getSearchResult(query: SearchQuery, v2: Boolean): Result<SearchResult> =
        if (v2) searchV2(query) else searchV1(query)

    private suspend fun searchV1(query: SearchQuery): Result<SearchResult> =
        serviceRepository
            .searchService(
                query = query,
                nextId = uiState.value.nextId,
                searchCount = SEARCH_COUNT,
                startIndex = uiState.value.items.size,
            )

    private suspend fun searchV2(query: SearchQuery): Result<SearchResult> =
        serviceRepository
            .searchServiceV2(
                query = query,
                nextId = uiState.value.nextId,
                searchCount = SEARCH_COUNT,
                startIndex = uiState.value.items.size,
                searchAfter = uiState.value.searchAfter
            )

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
                            address = item.region.region2,
                            distance = "0.5km",
                            deadLine = getDeadlineMessage(item.deadLine),
                            discountedPrice = item.discountedPrice.toInt(),
                            basePrice = item.basePrice.toInt(),
                            discountRatio = item.discountRatio,
                            memberStatus = memberStatusText,
                            booked = item.booked,
                            state = item.state,
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
                searchAfter = searchResult.searchAfter,
            )
    }

    private fun getDeadlineMessage(deadline: Date): String =
        getDeadlineMessageFromTimestamp(
            deadline = deadline.toTimeStamp().timeInMillis,
            today = Date.now().toTimeStamp().timeInMillis,
        )

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
        const val V2 = true
    }
}
