package com.ssavice.post_service

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.component.InputTransformations
import com.ssavice.designsystem.component.OutputTransformations
import com.ssavice.designsystem.component.SsaviceBackground
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.component.SsaviceButtonOutlined
import com.ssavice.designsystem.component.SsaviceDateSpinner
import com.ssavice.designsystem.component.SsaviceDropdown
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.TimeStamp
import com.ssavice.post_service.AddServiceScreenDefaults.CATEGORY_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.DESCRIPTION_PLACEHOLDER
import com.ssavice.post_service.AddServiceScreenDefaults.DESCRIPTION_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.DISCOUNT_RATIO_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.END_DATE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.HEADER_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.MAX_RECRUIT_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.MIN_RECRUIT_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.PRICE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.SERVICE_NAME_PLACEHOLDER
import com.ssavice.post_service.AddServiceScreenDefaults.SERVICE_NAME_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.START_DATE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.TAG_PLACEHOLDER
import com.ssavice.post_service.AddServiceScreenDefaults.TAG_TEXT

@Composable
fun AddServiceRoute(
    modifier: Modifier = Modifier,
    viewModel: AddServiceViewModel = hiltViewModel(),
    onSubmit: (Long) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 6.dp),
    ) {
        val state = viewModel.uiState.collectAsStateWithLifecycle()
        val serviceNameTextState = rememberTextFieldState(state.value.form.name)
        var category by remember { mutableStateOf(state.value.form.category) }
        val tagTextState = rememberTextFieldState(state.value.form.tag)
        val minRecruitTextState =
            rememberTextFieldState(
                state.value.form.minRecruit
                    .toString(),
            )
        val maxRecruitTextState =
            rememberTextFieldState(
                state.value.form.maxRecruit
                    .toString(),
            )
        val priceTextState =
            rememberTextFieldState(
                state.value.form.price
                    .toString(),
            )
        val discountRatioState =
            rememberTextFieldState(
                state.value.form.discountRatio
                    .toString(),
            )
        val descriptionTextState = rememberTextFieldState(state.value.form.description)

        LaunchedEffect(serviceNameTextState) {
            snapshotFlow { serviceNameTextState.text.toString() }
                .collect {
                    viewModel.onServiceNameChanged(it)
                }
        }
        LaunchedEffect(category) {
            snapshotFlow { category }
                .collect {
                    viewModel.onCategoryChanged(it)
                }
        }
        LaunchedEffect(tagTextState) {
            snapshotFlow { tagTextState.text.toString() }
                .collect {
                    viewModel.onTagChanged(it)
                }
        }
        LaunchedEffect(minRecruitTextState) {
            snapshotFlow { minRecruitTextState.text.toString().toIntOrNull() ?: 0 }
                .collect {
                    viewModel.onMinRecruitChanged(it)
                }
        }
        LaunchedEffect(maxRecruitTextState) {
            snapshotFlow { maxRecruitTextState.text.toString().toIntOrNull() ?: 0 }
                .collect {
                    viewModel.onMaxRecruitChanged(it)
                }
        }
        LaunchedEffect(priceTextState) {
            snapshotFlow { priceTextState.text.toString().toIntOrNull() ?: 0 }
                .collect {
                    viewModel.onPriceChanged(it)
                }
        }
        LaunchedEffect(descriptionTextState) {
            snapshotFlow { descriptionTextState.text.toString() }
                .collect {
                    viewModel.onDescriptionChanged(it)
                }
        }
        LaunchedEffect(state.value.submitState) {
            if (state.value.submitState is SubmitState.Success) {
                onSubmit((state.value.submitState as SubmitState.Success).serviceId)
            } else if (state.value.submitState is SubmitState.Dismiss) {
                onDismiss()
            }
        }

        AddServiceScreen(
            modifier =
                modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 10.dp),
            serviceNameTextState = serviceNameTextState,
            category = category,
            categories = state.value.form.categoryList,
            tagTextState = tagTextState,
            maxRecruitTextState = maxRecruitTextState,
            minRecruitTextState = minRecruitTextState,
            priceTextState = priceTextState,
            descriptionTextState = descriptionTextState,
            discountRatioState = discountRatioState,
            startDate = state.value.form.startDate,
            endDate = state.value.form.endDate,
            onStartDateChanged = viewModel::onStartDateChanged,
            onEndDateChanged = viewModel::onEndDateChanged,
            onCategoryChanged = { category = it },
            onSubmitClicked = viewModel::onSubmitButtonClicked,
            onDismissClicked = viewModel::onDismissButtonClicked,
            serviceNameTextStateErrorMessage = state.value.form.nameErrorMessage,
            categoryErrorMessage = state.value.form.categoryErrorMessage,
            tagTextStateErrorMessage = state.value.form.tagErrorMessage,
            minRecruitTextStateErrorMessage = state.value.form.minRecruitErrorMessage,
            maxRecruitTextStateErrorMessage = state.value.form.maxRecruitErrorMessage,
            priceTextStateErrorMessage = state.value.form.priceErrorMessage,
            discountRatioStateErrorMessage = state.value.form.discountRatioErrorMessage,
            descriptionTextStateErrorMessage = state.value.form.descriptionErrorMessage,
            startDateErrorMessage = state.value.form.startDateErrorMessage,
            endDateErrorMessage = state.value.form.endDateErrorMessage,
        )
    }
}

@Composable
fun AddServiceScreen(
    modifier: Modifier = Modifier,
    serviceNameTextState: TextFieldState,
    category: String,
    categories: List<String>,
    tagTextState: TextFieldState,
    maxRecruitTextState: TextFieldState,
    minRecruitTextState: TextFieldState,
    priceTextState: TextFieldState,
    discountRatioState: TextFieldState,
    descriptionTextState: TextFieldState,
    startDate: TimeStamp,
    endDate: TimeStamp,
    serviceNameTextStateErrorMessage: String? = null,
    categoryErrorMessage: String? = null,
    tagTextStateErrorMessage: String? = null,
    maxRecruitTextStateErrorMessage: String? = null,
    minRecruitTextStateErrorMessage: String? = null,
    priceTextStateErrorMessage: String? = null,
    discountRatioStateErrorMessage: String? = null,
    descriptionTextStateErrorMessage: String? = null,
    startDateErrorMessage: String? = null,
    endDateErrorMessage: String? = null,
    onStartDateChanged: (TimeStamp) -> Unit = {},
    onEndDateChanged: (TimeStamp) -> Unit = {},
    onCategoryChanged: (String) -> Unit = {},
    onSubmitClicked: () -> Unit = {},
    onDismissClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        ProvideTextStyle(MaterialTheme.typography.headlineLarge) {
            Text(HEADER_TEXT)
        }
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = serviceNameTextState,
            labelText = SERVICE_NAME_TEXT,
            placeholderText = SERVICE_NAME_PLACEHOLDER,
            inputTransformation = InputTransformation.maxLength(12),
            isError = serviceNameTextStateErrorMessage != null,
            errorMessage = serviceNameTextStateErrorMessage,
        )

        SsaviceDropdown(
            modifier = Modifier.fillMaxWidth(),
            options = categories,
            labelText = CATEGORY_TEXT,
            selectedOption = category,
            onOptionSelected = onCategoryChanged,
            isError = categoryErrorMessage != null,
            errorMessage = categoryErrorMessage,
        )

        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = tagTextState,
            labelText = TAG_TEXT,
            placeholderText = TAG_PLACEHOLDER,
            inputTransformation = InputTransformation.maxLength(12),
            isError = tagTextStateErrorMessage != null,
            errorMessage = tagTextStateErrorMessage,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = descriptionTextState,
            labelText = DESCRIPTION_TEXT,
            placeholderText = DESCRIPTION_PLACEHOLDER,
            multiLine = true,
            isError = descriptionTextStateErrorMessage != null,
            errorMessage = descriptionTextStateErrorMessage,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            SsaviceInputField(
                modifier = Modifier.weight(1f),
                state = minRecruitTextState,
                labelText = MIN_RECRUIT_TEXT,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                inputTransformation =
                    InputTransformation.maxLength(4).then(
                        InputTransformations.numberFormatInputTransformation.then(
                            InputTransformations.minMaxInputTransformation(1, 200),
                        ),
                    ),
                isError = minRecruitTextStateErrorMessage != null,
                errorMessage = minRecruitTextStateErrorMessage,
            )

            SsaviceInputField(
                modifier = Modifier.weight(1f),
                state = maxRecruitTextState,
                labelText = MAX_RECRUIT_TEXT,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                inputTransformation =
                    InputTransformation.maxLength(4).then(
                        InputTransformations.numberFormatInputTransformation.then(
                            InputTransformations.minMaxInputTransformation(1, 200),
                        ),
                    ),
                isError = maxRecruitTextStateErrorMessage != null,
                errorMessage = maxRecruitTextStateErrorMessage,
            )
        }
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = priceTextState,
            labelText = PRICE_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation =
                InputTransformation.maxLength(13).then(
                    InputTransformations.numberFormatInputTransformation,
                ),
            outputTransformation = OutputTransformations.formatNumberWithCommas,
            isError = priceTextStateErrorMessage != null,
            errorMessage = priceTextStateErrorMessage,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = discountRatioState,
            labelText = DISCOUNT_RATIO_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation =
                InputTransformation.maxLength(4).then(
                    InputTransformations.numberFormatInputTransformation.then(
                        InputTransformations.minMaxInputTransformation(0, 100),
                    ),
                ),
            isError = discountRatioStateErrorMessage != null,
            errorMessage = discountRatioStateErrorMessage,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            SsaviceDateSpinner(
                startDate.timeInMillis,
                onDateSelected = { onStartDateChanged(TimeStamp(it)) },
                modifier = Modifier.weight(1f, false),
                labelText = START_DATE_TEXT,
                isError = startDateErrorMessage != null,
                errorMessage = startDateErrorMessage,
            )
            SsaviceDateSpinner(
                endDate.timeInMillis,
                onDateSelected = { onEndDateChanged(TimeStamp(it)) },
                modifier = Modifier.weight(1f, false),
                labelText = END_DATE_TEXT,
                isError = endDateErrorMessage != null,
                errorMessage = endDateErrorMessage,
            )
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            SsaviceButtonOutlined(
                modifier = Modifier.weight(1f),
                text = "취소",
                onClick = onDismissClicked,
            )
            SsaviceButton(
                modifier = Modifier.weight(1f),
                text = "등록",
                onClick = onSubmitClicked,
            )
        }
    }
}

@Preview
@Composable
private fun AddServiceScreenPreview() {
    val serviceNameTextState = rememberTextFieldState("")
    val categories by remember { mutableStateOf(listOf("건강 / 생활", "식품", "취미", "스포츠", "요양", "문화")) }
    var category by remember { mutableStateOf(categories.first()) }
    val tagTextState = rememberTextFieldState("")
    val minRecruitTextState = rememberTextFieldState("1")
    val maxRecruitTextState = rememberTextFieldState("10")
    val priceTextState = rememberTextFieldState("0")
    val descriptionTextState = rememberTextFieldState("")
    val discountRatioState = rememberTextFieldState("0")

    var startDate by remember { mutableStateOf(TimeStamp(0L)) }
    var endDate by remember { mutableStateOf(TimeStamp(0L)) }

    SsaviceTheme {
        SsaviceBackground {
            Column(
                modifier =
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
            ) {
                AddServiceScreen(
                    modifier = Modifier,
                    serviceNameTextState = serviceNameTextState,
                    category = category,
                    categories = categories,
                    tagTextState = tagTextState,
                    minRecruitTextState = minRecruitTextState,
                    maxRecruitTextState = maxRecruitTextState,
                    priceTextState = priceTextState,
                    descriptionTextState = descriptionTextState,
                    onCategoryChanged = { category = it },
                    discountRatioState = discountRatioState,
                    startDate = startDate,
                    endDate = endDate,
                    onStartDateChanged = { startDate = it },
                    onEndDateChanged = { endDate = it },
                )
            }
        }
    }
}

internal object AddServiceScreenDefaults {
    const val HEADER_TEXT = "새 서비스 등록"

    const val SERVICE_NAME_TEXT = "서비스명"
    const val CATEGORY_TEXT = "카테고리"
    const val TAG_TEXT = "태그 (쉽표로 구분)"
    const val MIN_RECRUIT_TEXT = "최소 모집 인원"
    const val MAX_RECRUIT_TEXT = "최대 모집 인원"
    const val PRICE_TEXT = "가격 (원)"
    const val DISCOUNT_RATIO_TEXT = "할인율"
    const val DESCRIPTION_TEXT = "서비스 설명"
    const val START_DATE_TEXT = "시작일"
    const val END_DATE_TEXT = "종료일"

    const val SERVICE_NAME_PLACEHOLDER = "예: 요가 레슨"
    const val TAG_PLACEHOLDER = "예: 요가, 필라테스, 운동"
    const val DESCRIPTION_PLACEHOLDER = "상세한 설명을 작성해주세요."
}
