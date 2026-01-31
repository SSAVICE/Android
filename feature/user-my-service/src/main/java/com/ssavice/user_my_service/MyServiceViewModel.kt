package com.ssavice.user_my_service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.service.ServiceState
import com.ssavice.model.service.SortingOrder
import com.ssavice.model.user.UserServiceParticipationItem
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
        private val userInfoRepository: UserInfoRepository,
        private val serviceRepository: ServiceRepository,
    ) : ViewModel() {
        private var searchingProcess: Job? = null
        private val _uiState =
            MutableStateFlow(
                MyServiceUiState(
                    services = listOf(),
                    myServiceScreenStatus = MyServiceState.Initial,
                    hasNext = true,
                ),
            )

        val uiState = _uiState.asStateFlow()

        private fun mapItemToUiState(
            i: Int,
            nextId: Int,
            item: UserServiceParticipationItem,
        ): MyServiceItemUiState =
            MyServiceItemUiState(
                index = i + nextId,
                id = item.id,
                title = item.name,
                price = "%,d".format(item.price),
                thumbnailUrl = item.thumbnail,
                sellerName = item.sellerName,
                duration = "${item.startDate.toSimpleString()} - ${item.endDate.toSimpleString()}",
                cancellable = getIfStateCancellable(item.state),
                reviewable =
                    getIfStateReviewable(
                        item.state,
                        item.isReviewed,
                    ),
                sellerId = item.sellerId,
                state = item.state,
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
                    userInfoRepository
                        .getMyService(
                            page = null,
                            searchCount = 10,
                            sortingOrder = SortingOrder.POPULARITY,
                            serviceState = ServiceState.entries.getOrElse(uiState.value.searchTypeSelection) { ServiceState.ALL },
                        ).fold(
                            onSuccess = {
                                _uiState.update { origin ->
                                    val nextId = origin.services.size
                                    origin.copy(
                                        services =
                                            it.items.mapIndexed { i, item ->
                                                mapItemToUiState(i, nextId, item)
                                            },
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
                userInfoRepository
                    .getMyService(
                        page = uiState.value.nextPage,
                        searchCount = 10,
                        sortingOrder = SortingOrder.POPULARITY,
                        serviceState = ServiceState.ALL,
                    ).fold(
                        onSuccess = {
                            _uiState.update { origin ->
                                val nextId = origin.services.size
                                origin.copy(
                                    services =
                                        origin.services +
                                            it.items.mapIndexed { i, item ->
                                                mapItemToUiState(i, nextId, item)
                                            },
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
                serviceRepository.cancelService(id).fold(
                    onSuccess = {
                        loadService()
                    },
                    onFailure = {
                    },
                )
            }
        }

        private fun getIfStateCancellable(state: ServiceState): Boolean =
            when (state) {
                ServiceState.RECRUITING -> true
                ServiceState.SUCCEEDED -> false
                ServiceState.CANCELED -> false
                ServiceState.USER_CANCELED -> false
                ServiceState.COMPLETED -> false
                ServiceState.ALL -> false
            }

        private fun getIfStateReviewable(
            state: ServiceState,
            reviewed: Boolean,
        ): Boolean =
            when (state) {
                ServiceState.RECRUITING -> false
                ServiceState.SUCCEEDED -> false
                ServiceState.CANCELED -> false
                ServiceState.USER_CANCELED -> false
                ServiceState.COMPLETED -> !reviewed
                ServiceState.ALL -> false
            }
    }
