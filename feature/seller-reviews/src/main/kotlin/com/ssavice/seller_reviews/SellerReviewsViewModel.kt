package com.ssavice.seller_reviews

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.seller_reviews.navigation.SellerReviewsRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerReviewsViewModel @Inject constructor(
    private val sellerInfoRepository: SellerInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(SellerReviewUiState())
    val uiState = _uiState.asStateFlow()

    fun load() {
        _uiState.update {
            it.copy(
                reviewState = SellerReviewState.Loading
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            sellerInfoRepository.getSellerReviews(
                id = getIdFromSavedState(),
                page = 0,
                searchCount = 10
            ).fold(
                onSuccess = { reviews ->
                    _uiState.update {
                        it.copy(
                            reviews = reviews.reviews.mapIndexed { index, review ->
                                SellerReviewItemState(
                                    index = index,
                                    userName = review.userName,
                                    comment = review.comment,
                                    serviceName = review.serviceName,
                                    createdAt = review.createdAt.toSimpleString(),
                                    rate = review.rating
                                )
                            },
                            hasNext = reviews.hasNext,
                            nextPage = reviews.currentPage.toInt() + 1,
                            reviewState = SellerReviewState.Idle
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            reviewState = SellerReviewState.Error(e)
                        )
                    }
                }
            )
        }
    }

    fun initiate() {
        val id = getIdFromSavedState()
        _uiState.update {
            it.copy(
                sellerId = id,
            )
        }
        load()
    }

    fun loadMoreReviews() {
        _uiState.update {
            it.copy(
                reviewState = SellerReviewState.Loading
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            sellerInfoRepository.getSellerReviews(
                id = uiState.value.sellerId,
                page = uiState.value.nextPage,
                searchCount = 10
            ).fold(
                onSuccess = { reviews ->
                    _uiState.update {
                        it.copy(
                            reviews = it.reviews + reviews.reviews.mapIndexed { index, review ->
                                SellerReviewItemState(
                                    index = it.reviews.size + index,
                                    userName = review.userName,
                                    comment = review.comment,
                                    serviceName = review.serviceName,
                                    createdAt = review.createdAt.toSimpleString(),
                                    rate = review.rating
                                )
                            }
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            reviewState = SellerReviewState.Error(e)
                        )
                    }
                }
            )
        }
    }

    private fun getIdFromSavedState(): Long {
        return savedStateHandle.get<Long>(SellerReviewsRouteContract.ID) ?: let {
            throw IllegalArgumentException("id is required")
        }
    }
}
