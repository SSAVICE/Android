package com.ssavice.post_service

import com.ssavice.model.Category
import com.ssavice.model.TimeStamp

data class AddServiceUiState(
    val form: Form,
    val submitState: SubmitState,
)

data class Form(
    val name: String = "",
    val category: String = "",
    val tag: String = "",
    val minRecruit: Int,
    val maxRecruit: Int,
    val price: Int = 0,
    val discountRatio: Int = 0,
    val startDate: TimeStamp,
    val endDate: TimeStamp,
    val description: String = "",
    val categoryList: List<String> = Category.entries.map { it.value },
    val nameErrorMessage: String? = null,
    val categoryErrorMessage: String? = null,
    val tagErrorMessage: String? = null,
    val minRecruitErrorMessage: String? = null,
    val maxRecruitErrorMessage: String? = null,
    val priceErrorMessage: String? = null,
    val discountRatioErrorMessage: String? = null,
    val startDateErrorMessage: String? = null,
    val endDateErrorMessage: String? = null,
    val descriptionErrorMessage: String? = null,
)

sealed interface SubmitState {
    object Idle : SubmitState

    object Loading : SubmitState

    data class Success(
        val serviceId: Long,
    ) : SubmitState

    data class Error(
        val message: String,
    ) : SubmitState

    data object Dismiss : SubmitState
}
