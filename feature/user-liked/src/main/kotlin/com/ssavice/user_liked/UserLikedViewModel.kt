package com.ssavice.user_liked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.Date
import com.ssavice.model.service.UserWishListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserLikedViewModel
    @Inject
    constructor(
        private val userRepository: UserInfoRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(UserLikedUiState())
        private var searchingProcess: Job? = null
        val uiState = _uiState.asStateFlow()

        private fun getDeadlineMessage(deadline: Date): String {
            val deadline = deadline.toTimeStamp().timeInMillis
            val today = Date.now().toTimeStamp().timeInMillis

            val timeRemaining = deadline - today
            if (timeRemaining < 1000 * 60 * 60 * 24) {
                if (timeRemaining < 0) {
                    return "마감됨"
                }
                val hourRemaining = timeRemaining / (1000 * 60 * 60)

                return "${hourRemaining}시간 후 마감"
            } else {
                val dayRemaining = timeRemaining / (1000 * 60 * 60 * 24)
                return "${dayRemaining}일 후 마감"
            }
        }

        private fun getDiscountedMemberText(
            current: Int,
            minimum: Int,
        ): String {
            if (current >= minimum) return current.toString()
            return "$current/$minimum"
        }

        private fun mapItemToUiState(
            i: Int,
            nextId: Int,
            item: UserWishListItem,
        ): UserLikedItem =
            UserLikedItem(
                index = i + nextId,
                id = item.id,
                serviceName = item.name,
                price = item.basePrice.toInt(),
                imageUrl = item.image,
                sellerName = item.companyName,
                deadline = getDeadlineMessage(item.deadLine),
                tags = item.tag.split(',').map { it.trim() },
                locationInfo = "${item.region.region1} ${item.region.region2}",
                discountedPrice = item.discountedPrice.toInt(),
                participationInfo = getDiscountedMemberText(item.currentMember, item.minimumMember),
                discountRate = item.discountRatio,
            )

        fun loadService() {
            if (searchingProcess != null && (searchingProcess?.isActive == true)) {
                searchingProcess?.cancel()
            }

            _uiState.update {
                it.copy(
                    wishServiceScreenStatus = WishServiceState.Loading,
                )
            }

            searchingProcess =
                viewModelScope.launch(Dispatchers.IO) {
                    userRepository
                        .getWishList(
                            page = null,
                            searchCount = 10,
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
                                        wishServiceScreenStatus = WishServiceState.Loaded,
                                        hasNext = it.hasNext,
                                        nextPage = it.currentPage.toInt() + 1,
                                    )
                                }
                            },
                            onFailure = {
                                _uiState.update { origin ->
                                    origin.copy(
                                        wishServiceScreenStatus = WishServiceState.Error(it),
                                    )
                                }
                            },
                        )
                }
        }

        fun loadMoreService() {
            _uiState.update {
                it.copy(
                    wishServiceScreenStatus = WishServiceState.Loading,
                )
            }

            if (!uiState.value.hasNext) return

            viewModelScope.launch(Dispatchers.IO) {
                userRepository
                    .getWishList(
                        page = uiState.value.nextPage,
                        searchCount = 10,
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
                                    wishServiceScreenStatus = WishServiceState.Loaded,
                                    hasNext = it.hasNext,
                                    nextPage = it.currentPage.toInt() + 1,
                                )
                            }
                        },
                        onFailure = {
                            _uiState.update { origin ->
                                origin.copy(
                                    wishServiceScreenStatus = WishServiceState.Error(it),
                                )
                            }
                        },
                    )
            }
        }
    }
