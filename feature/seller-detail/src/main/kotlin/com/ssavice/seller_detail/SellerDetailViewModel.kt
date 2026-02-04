package com.ssavice.seller_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.common.getDeadlineMessageFromTimestamp
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.Date
import com.ssavice.model.enums.ServiceState
import com.ssavice.seller_detail.navigation.SellerDetailRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerDetailViewModel
    @Inject
    constructor(
        private val sellerRepository: SellerInfoRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SellerDetailUiState())
        val uiState = _uiState.asStateFlow()

        fun load() {
            _uiState.update {
                it.copy(
                    sellerDetailState = SellerDetailState.Loading,
                )
            }

            val id = getIdFromSavedStateHandle()

            if (id == -1L) {
                _uiState.update {
                    it.copy(
                        sellerDetailState = SellerDetailState.Error(IllegalStateException("판매자 조회에 실패했습니다.")),
                    )
                }
                return
            }

            viewModelScope.launch(Dispatchers.IO) {
                fetchAndUpdateSummary(id)
            }
            viewModelScope.launch(Dispatchers.IO) {
                fetchAndUpdateInfo(id)
            }
        }

        private fun getIdFromSavedStateHandle(): Long = savedStateHandle.get<Long>(SellerDetailRouteContract.ID) ?: -1L

        private suspend fun fetchAndUpdateSummary(id: Long) {
            sellerRepository
                .getSellerSummary(id)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            sellerInfo =
                                state.sellerInfo.copy(
                                    rate = it.companyRate,
                                    rateCount = it.rateCount,
                                    thumbnailUrl = it.companyImageUrl ?: "",
                                ),
                        )
                    }
                }
        }

        private fun mapPriceToString(price: Int): String = "￦%,d".format(price)

        private fun mapSchedule(
            state: ServiceState,
            deadline: Date,
            start: Date,
            end: Date,
        ): String =
            when (state) {
                ServiceState.RECRUITING, ServiceState.SUCCEEDED -> {
                    getDeadlineMessageFromTimestamp(
                        deadline = deadline.toTimeStamp().timeInMillis,
                        today = Date.now().toTimeStamp().timeInMillis,
                    )
                }

                ServiceState.COMPLETED -> {
                    "${start.toSimpleString()} - ${end.toSimpleString()}"
                }

                else -> {
                    ""
                }
            }

        private suspend fun fetchAndUpdateInfo(id: Long) {
            sellerRepository
                .getSellerDetail(id)
                .fold(
                    onSuccess = { detail ->
                        _uiState.update { state ->
                            state.copy(
                                sellerDetailState = SellerDetailState.Loaded,
                                sellerInfo =
                                    state.sellerInfo.copy(
                                        name = detail.sellerName,
                                        description = detail.description,
                                        detail = detail.detail,
                                        address = detail.address,
                                        detailAddress = detail.detailAddress,
                                        phoneNumber = detail.phoneNumber,
                                        imageUrls = listOf(),
                                        id = detail.id,
                                        region = detail.region,
                                    ),
                                serviceItems =
                                    detail.serviceItems.map {
                                        ServiceItemState(
                                            serviceId = it.id,
                                            name = it.name,
                                            thumbnailUrl = it.image,
                                            serviceState = it.state,
                                            category = it.category,
                                            price = mapPriceToString(it.discountedPrice.toInt()),
                                            dayState =
                                                mapSchedule(
                                                    it.state,
                                                    it.deadLine,
                                                    it.startDate,
                                                    it.endDate,
                                                ),
                                            region = "",
                                        )
                                    },
                                reviewItems = state.reviewItems,
                                businessInfo = state.businessInfo,
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.update {
                            it.copy(
                                sellerDetailState = SellerDetailState.Error(e),
                            )
                        }
                    },
                )
        }
    }
