package com.ssavice.seller_my_service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.common.DomainFormatter
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.seller.SellerServiceParticipationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyServiceViewModel
    @Inject
    constructor(
        private val sellerInfoRepository: SellerInfoRepository,
        private val serviceRepository: ServiceRepository,
    ) : ViewModel() {
        private var searchingProcess: Job? = null
        private val _uiState =
            MutableStateFlow(
                SellerMyServiceUiState(
                    services = listOf(),
                    myServiceScreenStatus = MyServiceState.Initial,
                    hasNext = true,
                ),
            )

        val uiState = _uiState.asStateFlow()

        private fun mapItemToUiState(
            i: Int,
            nextId: Int,
            item: SellerServiceParticipationItem,
        ): SellerMyServiceItemUiState =
            SellerMyServiceItemUiState(
                index = i + nextId,
                id = item.id,
                title = item.name,
                price = DomainFormatter.formatPrice(item.price),
                thumbnailUrl = item.thumbnail,
                duration = "${item.startDate.toSimpleString()} - ${item.endDate.toSimpleString()}",
                cancellable = item.state.cancellable,
                state = item.state,
                memberStatus = "${item.currentMemberCount}/${item.minimumMemberCount} (최대 ${item.maximumMemberCount})",
            )

        fun loadService() {
            if (searchingProcess != null && (searchingProcess?.isActive == true)) {
                searchingProcess?.cancel()
            }

            _uiState.update {
                it.copy(
                    myServiceScreenStatus = MyServiceState.Loading,
                )
            }

            searchingProcess =
                viewModelScope.launch(Dispatchers.IO) {
                    sellerInfoRepository
                        .getMyService(
                            page = null,
                            searchCount = 10,
                            sortingOrder = SortingOrder.POPULARITY,
                            serviceState = _uiState.value.searchingState.getOrElse(uiState.value.searchTypeSelection) { ServiceState.ALL },
                        ).fold(
                            onSuccess = {
                                val uiItems =
                                    it.items.mapIndexed { i, item ->
                                        mapItemToUiState(i, _uiState.value.services.size, item)
                                    }
                                _uiState.update { origin ->
                                    origin.copy(
                                        services =
                                        uiItems,
                                        myServiceScreenStatus = MyServiceState.Loaded,
                                        hasNext = it.hasNext,
                                        nextPage = it.currentPage.toInt() + 1,
                                    )
                                }
                            },
                            onFailure = {
                                _uiState.update { origin ->
                                    origin.copy(
                                        myServiceScreenStatus = MyServiceState.Error(it),
                                    )
                                }
                            },
                        )
                }
        }

        fun loadMoreService() {
            _uiState.update {
                it.copy(
                    myServiceScreenStatus = MyServiceState.Loading,
                )
            }

            if (!uiState.value.hasNext) return

            viewModelScope.launch(Dispatchers.IO) {
                sellerInfoRepository
                    .getMyService(
                        page = uiState.value.nextPage,
                        searchCount = 10,
                        sortingOrder = SortingOrder.POPULARITY,
                        serviceState = ServiceState.ALL,
                    ).fold(
                        onSuccess = {
                            _uiState.update { origin ->
                                val uiItems =
                                    it.items.mapIndexed { i, item ->
                                        mapItemToUiState(i, _uiState.value.services.size, item)
                                    }
                                origin.copy(
                                    services =
                                        origin.services + uiItems,
                                    myServiceScreenStatus = MyServiceState.Loaded,
                                    hasNext = it.hasNext,
                                    nextPage = it.currentPage.toInt() + 1,
                                )
                            }
                        },
                        onFailure = {
                            _uiState.update { origin ->
                                origin.copy(
                                    myServiceScreenStatus = MyServiceState.Error(it),
                                )
                            }
                        },
                    )
            }
        }

        fun onSearchingStateChange(index: Int) {
            _uiState.update {
                it.copy(
                    searchTypeSelection = index,
                )
            }
            loadService()
        }

        fun onCancelClick(id: Long) {
            _uiState.update {
                it.copy(
                    myServiceScreenStatus = MyServiceState.Loading,
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                serviceRepository.deleteService(id).fold(
                    onSuccess = {
                        loadService()
                    },
                    onFailure = {
                    },
                )
            }
        }
    }
