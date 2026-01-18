package com.ssavice.post_service

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.snapshots.toInt
import androidx.compose.ui.util.fastLastOrNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.model.Date
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.RegionInfo
import com.ssavice.model.TimeStamp
import com.ssavice.model.service.ServiceAddForm
import com.ssavice.ui.model.AndroidResizableImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.apply
import kotlin.math.ceil

@HiltViewModel
class AddServiceViewModel
@Inject
constructor(
    private val serviceRepository: ServiceRepository,
    @ApplicationContext private val context: Context
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
                        deadline = TimeStamp(Calendar.getInstance().timeInMillis),
                        description = DESCRIPTION_DEFAULT,
                    ),
                submitState = SubmitState.Idle,
                imageState = ImageState(
                    pictureList = listOf(),
                ),
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

    private fun getDiscountRate(discountedPrice: Int, basePrice: Int, formerRate: Int): Int {
        if (basePrice == 0) return formerRate
        return 100 - ceil(discountedPrice.toLong() * 100.0 / basePrice).toInt()
    }

    fun onPriceChanged(price: Int) {
        val newDiscounted = price - price.toLong() * uiState.value.form.discountRatio / 100
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        price = price,
                        discountedPrice = newDiscounted.toInt()
                    ),
            )
    }

    fun onDiscountedPriceChanged(price: Int) {
        val newRate = getDiscountRate(
            price, uiState.value.form.price,
            uiState.value.form.discountRatio
        )

        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        discountedPrice = price,
                        discountRatio = newRate,
                    ),
            )
    }

    fun onDiscountRatioChanged(discount: Int) {
        val newDiscountedPrice: Int = if (discount != uiState.value.form.discountRatio) {
            (uiState.value.form.price - uiState.value.form.price.toLong() * discount / 100).toInt()
        } else {
            uiState.value.form.discountedPrice
        }

        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        discountRatio = discount,
                        discountedPrice = newDiscountedPrice
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

    fun onDeadlineChanged(deadline: TimeStamp) {
        uiState.value =
            uiState.value.copy(
                form =
                    uiState.value.form.copy(
                        deadline = deadline,
                    ),
            )
    }

    fun onSubmitButtonClicked() {
        // 빈 필드 여부 확인
        val validateEmptyForm = checkEmptyField()

        uiState.value =
            uiState.value.copy(
                form = validateEmptyForm.first,
                submitState = uiState.value.submitState,
            )

        // 모집 기간 유효성 검증
        val validateInvalidTime = checkInvalidDueTime()
        uiState.value =
            uiState.value.copy(
                form = validateInvalidTime.first,
                submitState = uiState.value.submitState,
            )

        // 모집 인원 유효성 검증
        val validateInvalidateRecruit = checkInvalidateRecruit()
        uiState.value =
            uiState.value.copy(
                form = validateInvalidateRecruit.first,
                submitState = uiState.value.submitState,
            )

        if (!validateEmptyForm.second && !validateInvalidTime.second && !validateInvalidateRecruit.second) {
            submit()
        }
    }

    fun onDismissButtonClicked() {
        uiState.value =
            uiState.value.copy(
                submitState = SubmitState.Dismiss,
            )
    }

    fun onImageSelected(uri: Uri) {
        // 이미 업로드 된 이미지는 추가 안함. 업로드 실패 한정 재업로드
        if (uiState.value.imageState.pictureList.any {
                it.uri == uri && it.progress !is ImageUploadProgress.Error
            }) {
            return
        }

        val image = AndroidResizableImage.fromUri(
            uri = uri, targetSizeInBytes = IMAGE_SIZE_BYTES, context = context
        )
        if (image == null) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.addServiceImage(
                image
            ).collect { progress ->
                uiState.update {
                    it.copy(
                        imageState = it.imageState.add(
                            UploadingImage(
                                uri = uri,
                                progress = progress
                            )
                        )
                    )
                }
            }
        }
    }

    fun onImageRemoveButtonClicked(i: Int) {
        uiState.update {
            it.copy(
                imageState = it.imageState.removeAt(i)
            )
        }
    }

    private fun submit() {
        uiState.value = uiState.value.copy(submitState = SubmitState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository
                .postService(
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
                            uiState.value.form.discountedPrice,
                        deadLine = Date.parse(uiState.value.form.deadline),
                        imageObjectKeys = uiState.value.imageState.pictureList.mapNotNull {
                            (it.progress as? ImageUploadProgress.Done)?.objectKey
                        },
                    ),
                ).fold(
                    onSuccess = {
                        uiState.value =
                            uiState.value.copy(submitState = SubmitState.Success(it))
                    },
                    onFailure = {
                        uiState.value =
                            uiState.value.copy(
                                submitState =
                                    SubmitState.Error(it.message ?: "Unknown error"),
                            )
                    },
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
        val deadLineMessage: String?
        val startDateMessage: String?
        val endDateMessage: String?

        // 태그는 필수가 아님
        val tagMessage = null
        // 설몀란은 필수가 아님
        val descriptionMessage = null

        if (uiState.value.form.deadline == TimeStamp(0L)) {
            hasError = true
            startDateMessage = "시작일을 선택해주세요"
        } else {
            startDateMessage = null
        }
        if (uiState.value.form.startDate == TimeStamp(0L)) {
            hasError = true
            deadLineMessage = "마감일 선택해주세요"
        } else {
            deadLineMessage = null
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
                descriptionErrorMessage = descriptionMessage,
                deadlineErrorMessage = deadLineMessage,
                startDateErrorMessage = startDateMessage,
                endDateErrorMessage = endDateMessage,
            )

        return Pair(form, hasError)
    }

    private fun checkInvalidDueTime(): Pair<Form, Boolean> {
        var form = uiState.value.form
        var hasError = false
        val deadline = Date.parse(uiState.value.form.deadline)
        val startDate = Date.parse(uiState.value.form.startDate)
        val endDate = Date.parse(uiState.value.form.endDate)


        if ((uiState.value.form.deadline != TimeStamp(0L)) &&
            (deadline <= Date.now())
        ) {
            form =
                form.copy(
                    deadlineErrorMessage = "마감일은 현재 날짜 이후여야 합니다",
                )
            hasError = true
        }
        if ((uiState.value.form.startDate != TimeStamp(0L)) &&
            (startDate < deadline)
        ) {
            form =
                form.copy(
                    startDateErrorMessage = "시작일은 마감일 이후여야 합니다",
                )
            hasError = true
        }
        if ((uiState.value.form.endDate != TimeStamp(0L)) &&
            (endDate <= startDate)
        ) {
            form =
                form.copy(
                    endDateErrorMessage = "종료일은 시작일 이후여야 합니다",
                )
            hasError = true
        }
        return Pair(form, hasError)
    }

    private fun checkInvalidateRecruit(): Pair<Form, Boolean> {
        var form = uiState.value.form
        var hasError = false

        if ((uiState.value.form.minRecruit <= 0)) {
            form =
                form.copy(
                    minRecruitErrorMessage =
                        "최소 모집인원은 0보다 커야합니다.",
                )
            hasError = true
        }
        if ((uiState.value.form.minRecruit > uiState.value.form.maxRecruit)) {
            form =
                form.copy(
                    minRecruitErrorMessage =
                        "최소 모집인원은 최대 모집인원 보다 작아야 합니다.",
                    maxRecruitErrorMessage =
                        "최소 모집인원은 최대 모집인원 보다 작아야 합니다.",
                )
            hasError = true
        }
        return Pair(form, hasError)
    }

    companion object {
        private const val TAG = "AddServiceViewModel"
        private const val DESCRIPTION_DEFAULT = ""

        const val IMAGE_SIZE_BYTES = 1024 * 1024 * 3L
    }
}
