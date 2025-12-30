package com.ssavice.post_service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.model.Date
import com.ssavice.model.RegionInfo
import com.ssavice.model.TimeStamp
import com.ssavice.model.service.ServiceAddForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddServiceViewModel
@Inject
constructor(
    private val serviceRepository: ServiceRepository,
) : ViewModel() {
    val uiState =
        MutableStateFlow(
            AddServiceUiState(
                form =
                    Form(
                        minRecruit = 1,
                        maxRecruit = 10,
                        startDate = TimeStamp(Calendar.getInstance().timeInMillis),
                        endDate = TimeStamp(Calendar.getInstance().timeInMillis),
                    ),
                submitState = SubmitState.Idle,
            ),
        )

    fun onServiceNameChanged(serviceName: String) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        name = serviceName,
                    ),
            )
    }

    fun onCategoryChanged(category: String) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        category = category,
                    ),
            )
    }

    fun onTagChanged(tags: String) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        tag = tags,
                    ),
            )
    }

    fun onMaxRecruitChanged(maxRecruit: Int) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        maxRecruit = maxRecruit,
                    ),
            )
    }

    fun onMinRecruitChanged(minRecruit: Int) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        minRecruit = minRecruit,
                    ),
            )
    }

    fun onPriceChanged(price: Int) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        price = price,
                    ),
            )
    }

    fun onDescriptionChanged(description: String) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        description = description,
                    ),
            )
    }

    fun onStartDateChanged(startDate: TimeStamp) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        startDate = startDate,
                    ),
            )
    }

    fun onEndDateChanged(endDate: TimeStamp) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        endDate = endDate,
                    ),
            )
    }

    fun onSubmitButtonClicked() {
        val validateEmptyForm = checkEmptyField()

        uiState.value =
            uiState.value.copy(
                form = validateEmptyForm.first,
                submitState = uiState.value.submitState,
            )

        val validateInvalidTime = checkInvalidDueTime()
        uiState.value =
            uiState.value.copy(
                form = validateInvalidTime.first,
                submitState = uiState.value.submitState,
            )

        if (!validateEmptyForm.second && !validateInvalidTime.second) submit()
    }

    fun onDismissButtonClicked() {
        uiState.value =
            uiState.value.copy(
                submitState = SubmitState.Dismiss,
            )
    }

    private fun submit() {
        uiState.value = uiState.value.copy(submitState = SubmitState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.postService(
                ServiceAddForm(
                    name = uiState.value.form.name,
                    category = uiState.value.form.category,
                    imageCount = 0,
                    minimumRecruit = uiState.value.form.minRecruit,
                    maximumRecruit = uiState.value.form.maxRecruit,
                    basePrice = uiState.value.form.price,
                    discountRatio = uiState.value.form.discountRatio,
                    tag = uiState.value.form.tag,
                    endDate = Date.parse(uiState.value.form.endDate),
                    startDate = Date.parse(uiState.value.form.startDate),
                    description = uiState.value.form.description,
                    region = RegionInfo.demo,
                    discountedPrice =
                        (uiState.value.form.price * (100 - uiState.value.form.discountRatio) / 100),
                    deadLine = Date.parse(uiState.value.form.startDate),
                )
            ).fold(
                onSuccess = {
                    uiState.value =
                        uiState.value.copy(submitState = SubmitState.Success(it))
                },
                onFailure = {
                    uiState.value =
                        uiState.value.copy(
                            submitState =
                                SubmitState.Error(it.message ?: "Unknown error")
                        )

                }
            )
        }
    }

    private fun checkEmptyField(): Pair<Form, Boolean> {
        var hasError = false

        fun validateAndGetMessage(
            value: String,
            errorMessage: String,
        ): String? =
            if (value.isEmpty()) {
                hasError = true
                errorMessage
            } else {
                null
            }

        fun validateAndGetMessage(
            value: Long,
            errorMessage: String,
        ): String? =
            if (value == 0L) {
                hasError = true
                errorMessage
            } else {
                null
            }

        val serviceNameMessage: String? =
            validateAndGetMessage(uiState.value.form.name, "서비스명을 입력해주세요")
        val categoryMessage: String? =
            validateAndGetMessage(uiState.value.form.category, "카테고리를 선택해주세요")
        val minRecruitMessage: String? =
            validateAndGetMessage(
                uiState.value.form.minRecruit
                    .toLong(),
                "최소 모집 인원을 정해주세요",
            )
        val maxRecruitMessage: String? =
            validateAndGetMessage(
                uiState.value.form.maxRecruit
                    .toLong(),
                "최대 모집 인원을 정해주세요",
            )
        val priceMessage: String? =
            validateAndGetMessage(
                uiState.value.form.price
                    .toLong(),
                "가격을 정해주세요",
            )
        val startDateMessage: String?
        val endDateMessage: String?

        // 태그는 필수가 아님
        val tagMessage = null
        // 할인율은 아직 확인 X
        val discountRatioMessage = null
        // 설몀란은 필수가 아님
        val descriptionMessage = null

        if (uiState.value.form.startDate == TimeStamp(0L)) {
            hasError = true
            startDateMessage = "시작일을 선택해주세요"
        } else {
            startDateMessage = null
        }
        if (uiState.value.form.endDate == TimeStamp(0L)) {
            hasError = true
            endDateMessage = "종료일을 선택해주세요"
        } else {
            endDateMessage = null
        }

        val form =
            uiState.value.form.copy(
                nameErrorMessage = serviceNameMessage,
                categoryErrorMessage = categoryMessage,
                tagErrorMessage = tagMessage,
                minRecruitErrorMessage = minRecruitMessage,
                maxRecruitErrorMessage = maxRecruitMessage,
                priceErrorMessage = priceMessage,
                discountRatioErrorMessage = discountRatioMessage,
                descriptionErrorMessage = descriptionMessage,
                startDateErrorMessage = startDateMessage,
                endDateErrorMessage = endDateMessage,
            )

        return Pair(form, hasError)
    }

    private fun checkInvalidDueTime(): Pair<Form, Boolean> {
        var form = uiState.value.form
        var hasError = false
        if ((uiState.value.form.startDate != TimeStamp(0L))
            && (uiState.value.form.startDate.timeInMillis <= Date.now().toTimeStamp().timeInMillis)
        ) {
            form = form.copy(
                startDateErrorMessage = "시작일은 현재 날짜 이후여야 합니다"
            )
            hasError = true
        }
        if ((uiState.value.form.endDate != TimeStamp(0L))
            && (uiState.value.form.endDate.timeInMillis <= uiState.value.form.startDate.timeInMillis)
        ) {
            form = form.copy(
                endDateErrorMessage = "종료일은 시작일 이후여야 합니다"
            )
            hasError = true
        }
        return Pair(form, hasError)
    }

    companion object {
        private const val TAG = "AddServiceViewModel"
        private const val MIN_RECRUIT_DEFAULT = 1
        private const val MAX_RECRUIT_DEFAULT = 100
        private const val PRICE_DEFAULT = 0
        private const val DISCOUNT_RATIO_DEFAULT = 0
        private const val DESCRIPTION_DEFAULT = ""

        private const val NO_FIELD_ERROR_MESSAGE = "해당 필드는 필수입니다"
    }
}
