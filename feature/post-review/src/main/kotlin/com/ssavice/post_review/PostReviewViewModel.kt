package com.ssavice.post_review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.service_detail.navigation.PostReviewRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostReviewViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val serviceRepository: ServiceRepository,
    ) : ViewModel() {
        val state =
            MutableStateFlow(
                PostReviewUiState(
                    serviceName = "",
                    serviceId = -1,
                    serviceThumbnailUrl = "",
                ),
            )

        private fun getFromSavedStateFlow() {
            val id = savedStateHandle.get<Long>(PostReviewRouteContract.ID)
            val name = savedStateHandle.get<String>(PostReviewRouteContract.NAME)
            val thumbnailUrl = savedStateHandle.get<String>(PostReviewRouteContract.THUMBNAIL_URL)

            if (id == null) {
                state.update {
                    it.copy(
                        reviewPostState = ReviewPostState.Failure(IllegalStateException("serviceId is null")),
                    )
                }
            } else {
                state.update {
                    it.copy(
                        serviceId = id,
                        serviceName = name ?: "",
                        serviceThumbnailUrl = thumbnailUrl ?: "",
                    )
                }
            }
        }

        fun init() {
            getFromSavedStateFlow()
        }

        fun postReview(
            serviceId: Long,
            rating: Int,
            review: String,
        ) {
            state.update {
                it.copy(
                    reviewPostState = ReviewPostState.Loading,
                )
            }

            viewModelScope.launch(Dispatchers.IO) {
                serviceRepository
                    .reviewService(
                        serviceId,
                        review,
                        rating,
                    ).fold(
                        onSuccess = {
                            state.update {
                                it.copy(
                                    reviewPostState = ReviewPostState.Success,
                                )
                            }
                        },
                        onFailure = { e ->
                            state.update {
                                it.copy(
                                    reviewPostState = ReviewPostState.Failure(e),
                                )
                            }
                        },
                    )
            }

            state.update {
                it.copy(
                    reviewPostState = ReviewPostState.Success,
                )
            }
        }
    }
