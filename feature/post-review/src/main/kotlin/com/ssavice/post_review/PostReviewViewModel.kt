package com.ssavice.post_review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ssavice.service_detail.navigation.PostReviewRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostReviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val state = MutableStateFlow(
        PostReviewUiState(
            serviceName = "",
            serviceId = -1,
            serviceThumbnailUrl = ""
        )
    )

    private fun getFromSavedStateFlow() {
        val id = savedStateHandle.get<Long>(PostReviewRouteContract.ID)
        val name = savedStateHandle.get<String>(PostReviewRouteContract.NAME)
        val thumbnailUrl = savedStateHandle.get<String>(PostReviewRouteContract.THUMBNAIL_URL)

        if (id == null) {
            state.update {
                it.copy(
                    reviewPostState = ReviewPostState.Failure(IllegalStateException("serviceId is null"))
                )
            }
        } else {
            state.update {
                it.copy(
                    serviceId = id,
                    serviceName = name ?: "",
                    serviceThumbnailUrl = thumbnailUrl ?: ""
                )
            }
        }
    }

    fun init() {
        getFromSavedStateFlow()
    }


    fun postReview(serviceId: Long, rating: Int, review: String) {
        state.update {
            it.copy(
                reviewPostState = ReviewPostState.Loading
            )
        }

        // TODO: Update 로직 구현

        state.update {
            it.copy(
                reviewPostState = ReviewPostState.Success
            )
        }

    }

}
