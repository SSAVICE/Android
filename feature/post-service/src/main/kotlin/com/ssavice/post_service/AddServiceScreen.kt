package com.ssavice.post_service

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.component.InputTransformations
import com.ssavice.designsystem.component.LabeledComponent
import com.ssavice.designsystem.component.OutputTransformations
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.component.SsaviceButtonOutlined
import com.ssavice.designsystem.component.SsaviceDateSpinner
import com.ssavice.designsystem.component.SsaviceDropdown
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.TimeStamp
import com.ssavice.post_service.AddServiceScreenDefaults.ADD_IMAGE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.BASIC_INFORMATION_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.CATEGORY_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.DEADLINE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.DESCRIPTION_PLACEHOLDER
import com.ssavice.post_service.AddServiceScreenDefaults.DESCRIPTION_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.DISCOUNTED_PRICE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.DISCOUNT_RATIO_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.END_DATE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.MAX_RECRUIT_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.MIN_RECRUIT_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.PRICE_INFORMATION_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.PRICE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.SCHEDULE_INFORMATION_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.SERVICE_NAME_PLACEHOLDER
import com.ssavice.post_service.AddServiceScreenDefaults.SERVICE_NAME_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.START_DATE_TEXT
import com.ssavice.post_service.AddServiceScreenDefaults.TAG_PLACEHOLDER
import com.ssavice.post_service.AddServiceScreenDefaults.TAG_TEXT
import com.ssavice.ui.uploadImage.ImageWithUploadState

@Composable
fun AddServiceRoute(
    modifier: Modifier = Modifier,
    viewModel: AddServiceViewModel = hiltViewModel(),
    onSubmit: (Long) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(
        state.submitState
    ) {
        if (state.submitState is SubmitState.Success) {
            (state.submitState as? SubmitState.Success)?.run {
                onSubmit(this.serviceId)
            }
        } else if (state.submitState is SubmitState.Dismiss) {
            onDismiss()
        }
    }

    AddServiceScreen(
        modifier = modifier,
        state = state,
        onSubmit = onSubmit,
        onDismiss = viewModel::onDismissButtonClicked,
        onBackButtonClicked = onDismiss,
        onSubmitButtonClicked = viewModel::onSubmitButtonClicked,
        onImageSelected = viewModel::onImageSelected,
        onImageRemoveClicked = viewModel::onImageRemoveButtonClicked,
        onServiceNameChanged = viewModel::onServiceNameChanged,
        onCategoryChanged = viewModel::onCategoryChanged,
        onTagChanged = viewModel::onTagChanged,
        onMaxRecruitChanged = viewModel::onMaxRecruitChanged,
        onMinRecruitChanged = viewModel::onMinRecruitChanged,
        onPriceChanged = viewModel::onPriceChanged,
        onDiscountedPriceChanged = viewModel::onDiscountedPriceChanged,
        onDiscountRatioChanged = viewModel::onDiscountRatioChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onDeadlineChanged = viewModel::onDeadlineChanged,
        onStartDateChanged = viewModel::onStartDateChanged,
        onEndDateChanged = viewModel::onEndDateChanged,
    )
}

@Composable
fun AddServiceScreen(
    modifier: Modifier = Modifier,
    state: AddServiceUiState,
    onSubmit: (Long) -> Unit = {},
    onDismiss: () -> Unit = {},
    onBackButtonClicked: () -> Unit = {},
    onSubmitButtonClicked: () -> Unit = {},
    onImageSelected: (uri: Uri) -> Unit = {},
    onImageRemoveClicked: (Int) -> Unit = {},
    onServiceNameChanged: (String) -> Unit = {},
    onCategoryChanged: (String) -> Unit = {},
    onTagChanged: (String) -> Unit = {},
    onMaxRecruitChanged: (Int) -> Unit = {},
    onMinRecruitChanged: (Int) -> Unit = {},
    onPriceChanged: (Int) -> Unit = {},
    onDiscountedPriceChanged: (Int) -> Unit = {},
    onDiscountRatioChanged: (Int) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    onDeadlineChanged: (TimeStamp) -> Unit = {},
    onStartDateChanged: (TimeStamp) -> Unit = {},
    onEndDateChanged: (TimeStamp) -> Unit = {},
) {
    val serviceNameTextState = rememberTextFieldState(state.form.name)
    var category by remember { mutableStateOf(state.form.category) }
    val tagTextState = rememberTextFieldState(state.form.tag)
    val minRecruitTextState =
        rememberTextFieldState(
            state.form.minRecruit
                .toString(),
        )
    val maxRecruitTextState =
        rememberTextFieldState(
            state.form.maxRecruit
                .toString(),
        )
    val priceTextState =
        rememberTextFieldState(
            state.form.price
                .toString(),
        )
    val discountedPriceTextState =
        rememberTextFieldState(
            state.form.discountRatio
                .toString(),
        )
    val descriptionTextState = rememberTextFieldState(state.form.description)

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    onImageSelected(uri)
                }
            },
        )

    LaunchedEffect(serviceNameTextState) {
        snapshotFlow { serviceNameTextState.text.toString() }
            .collect {
                onServiceNameChanged(it)
            }
    }
    LaunchedEffect(category) {
        snapshotFlow { category }
            .collect {
                onCategoryChanged(it)
            }
    }
    LaunchedEffect(tagTextState) {
        snapshotFlow { tagTextState.text.toString() }
            .collect {
                onTagChanged(it)
            }
    }
    LaunchedEffect(minRecruitTextState) {
        snapshotFlow { minRecruitTextState.text.toString().toIntOrNull() ?: 0 }
            .collect {
                onMinRecruitChanged(it)
            }
    }
    LaunchedEffect(maxRecruitTextState) {
        snapshotFlow { maxRecruitTextState.text.toString().toIntOrNull() ?: 0 }
            .collect {
                onMaxRecruitChanged(it)
            }
    }
    LaunchedEffect(priceTextState) {
        snapshotFlow { priceTextState.text.toString().toIntOrNull() ?: 0 }
            .collect {
                onPriceChanged(it)
            }
    }
    LaunchedEffect(descriptionTextState) {
        snapshotFlow { descriptionTextState.text.toString() }
            .collect {
                onDescriptionChanged(it)
            }
    }
    LaunchedEffect(discountedPriceTextState) {
        snapshotFlow { discountedPriceTextState.text.toString().toIntOrNull() ?: 0 }
            .collect {
                onDiscountedPriceChanged(it)
            }
    }
    LaunchedEffect(state.submitState) {
        if (state.submitState is SubmitState.Success) {
            onSubmit(state.submitState.serviceId)
        } else if (state.submitState is SubmitState.Dismiss) {
            onDismiss()
        }
    }
    LaunchedEffect(state.form.discountedPrice) {
        discountedPriceTextState.edit {
            replace(
                0, discountedPriceTextState.text.length,
                state.form.discountedPrice.toString()
            )
        }
    }

    val enabled = state.imageState.pictureList.fastAll{
        it.progress is ImageUploadProgress.Done
                || it.progress is ImageUploadProgress.Error
    }

    Column(
        modifier =
            modifier
                .padding(horizontal = 6.dp),
    ) {
        Spacer(Modifier.height(10.dp))
        Text(style = MaterialTheme.typography.titleMedium, text = ADD_IMAGE_TEXT,
            modifier = Modifier.padding(
                horizontal = 14.dp
            ))
        // 이미지 선택기
        ImageSelector(
            imageState = state.imageState,
            onAddClicked = {
                photoPickerLauncher
                    .launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
            },
            onRemoveImageClicked = onImageRemoveClicked
        )
        HorizontalDivider(Modifier.padding(10.dp))

        Spacer(Modifier.height(15.dp))

        // 서비스 입력 폼
        AddServiceForm(
            modifier =
                Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 10.dp),
            serviceNameTextState = serviceNameTextState,
            category = category,
            categories = state.form.categoryList,
            tagTextState = tagTextState,
            maxRecruitTextState = maxRecruitTextState,
            minRecruitTextState = minRecruitTextState,
            priceTextState = priceTextState,
            discountRatio = state.form.discountRatio.toFloat(),
            descriptionTextState = descriptionTextState,
            startDate = state.form.startDate,
            endDate = state.form.endDate,
            deadline = state.form.deadline,
            discountedPriceTextState = discountedPriceTextState,
            onDiscountRatioChange = { onDiscountRatioChanged(it.toInt()) },
            onDeadlineChanged = onDeadlineChanged,
            onStartDateChanged = onStartDateChanged,
            onEndDateChanged = onEndDateChanged,
            onCategoryChanged = { category = it },
            onSubmitClicked = onSubmitButtonClicked,
            onDismissClicked = onBackButtonClicked,
            enabled = enabled,
            serviceNameTextStateErrorMessage = state.form.nameErrorMessage,
            categoryErrorMessage = state.form.categoryErrorMessage,
            tagTextStateErrorMessage = state.form.tagErrorMessage,
            minRecruitTextStateErrorMessage = state.form.minRecruitErrorMessage,
            maxRecruitTextStateErrorMessage = state.form.maxRecruitErrorMessage,
            priceTextStateErrorMessage = state.form.priceErrorMessage,
            discountRatioStateErrorMessage = state.form.discountRatioErrorMessage,
            descriptionTextStateErrorMessage = state.form.descriptionErrorMessage,
            startDateErrorMessage = state.form.startDateErrorMessage,
            endDateErrorMessage = state.form.endDateErrorMessage,
            deadlineErrorMessage = state.form.deadlineErrorMessage
        )
    }
}

@Composable
fun ImageSelector(
    modifier: Modifier = Modifier,
    onAddClicked: () -> Unit = {},
    onRemoveImageClicked: (Int) -> Unit = {},
    imageState: ImageState,
) {
    var selectedImage by remember { mutableIntStateOf(-1) }
    var lastObservedListSize by remember { mutableIntStateOf(0) }
    LaunchedEffect(imageState) {
        if (imageState.pictureList.size != lastObservedListSize) {
            selectedImage = -1
            lastObservedListSize = imageState.pictureList.size
        }
    }
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
    ) {
        item(key = "add_button") {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clickable { onAddClicked() }
                    .padding(10.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                ,
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Add Image",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${imageState.pictureList.size}/10", // 최대 개수 예시
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        items(
            imageState.pictureList.size,
            key = { i -> imageState.pictureList[i].uri }) { i ->
            val picture = imageState.pictureList[i]
            val selected = selectedImage == i
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .animateItem()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    ImageWithUploadState(
                        baseImageUrl = null,
                        uploadingImageUrl = picture.uri.toString(),
                        uploadState = picture.progress,
                        contentDescription = "Service Picture $i"
                    )
                    AnimatedVisibility(
                        visible = selected,
                        enter = fadeIn(animationSpec = tween(200)),
                        exit = fadeOut(animationSpec = tween(200))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                enabled = picture.progress is ImageUploadProgress.Done,
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                selectedImage = if (selected) -1 else i
                            },
                    )
                }

                if(selected) {
                    IconButton(
                        onClick = {
                            selectedImage = -1
                            onRemoveImageClicked(i)
                                  },
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .background(
                                color = MaterialTheme.colorScheme.error,
                                shape = CircleShape
                            )
                            .graphicsLayer(clip = false)
                            .padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove Image",
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddServiceForm(
    modifier: Modifier = Modifier,
    serviceNameTextState: TextFieldState,
    category: String,
    categories: List<String>,
    tagTextState: TextFieldState,
    maxRecruitTextState: TextFieldState,
    minRecruitTextState: TextFieldState,
    priceTextState: TextFieldState,
    discountedPriceTextState: TextFieldState,
    discountRatio: Float,
    descriptionTextState: TextFieldState,
    deadline: TimeStamp,
    startDate: TimeStamp,
    endDate: TimeStamp,
    enabled: Boolean = true,
    serviceNameTextStateErrorMessage: String? = null,
    categoryErrorMessage: String? = null,
    tagTextStateErrorMessage: String? = null,
    maxRecruitTextStateErrorMessage: String? = null,
    minRecruitTextStateErrorMessage: String? = null,
    priceTextStateErrorMessage: String? = null,
    descriptionTextStateErrorMessage: String? = null,
    discountRatioStateErrorMessage: String? = null,
    deadlineErrorMessage: String? = null,
    startDateErrorMessage: String? = null,
    endDateErrorMessage: String? = null,
    onDiscountRatioChange: (Float) -> Unit = {},
    onDeadlineChanged: (TimeStamp) -> Unit = {},
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
        Text(style = MaterialTheme.typography.titleMedium, text = BASIC_INFORMATION_TEXT)
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

        Spacer(Modifier.height(10.dp))
        Text(style = MaterialTheme.typography.titleMedium, text = PRICE_INFORMATION_TEXT)

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

        Column {
            LabeledComponent(
                Modifier.fillMaxWidth(),
                labelText = DISCOUNT_RATIO_TEXT,
                isError = discountRatioStateErrorMessage != null,
                errorMessage = null
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        modifier = Modifier.weight(1f),
                        value = discountRatio,
                        valueRange = (0f..100f),
                        onValueChange = onDiscountRatioChange,
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = "${discountRatio.toInt()}%",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }

        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = discountedPriceTextState,
            labelText = DISCOUNTED_PRICE_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation =
                InputTransformation.maxLength(13).then(
                    InputTransformations.numberFormatInputTransformation.then(
                        InputTransformations
                            .minMaxInputTransformation
                                (0, priceTextState.text.toString().toLongOrNull() ?: 0L),
                    ),
                ),
            outputTransformation = OutputTransformations.formatNumberWithCommas,
            isError = discountRatioStateErrorMessage != null,
            errorMessage = discountRatioStateErrorMessage,
        )


        Spacer(Modifier.height(10.dp))
        Text(style = MaterialTheme.typography.titleMedium, text = SCHEDULE_INFORMATION_TEXT)

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
                            InputTransformations.minMaxInputTransformation(0, 200),
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
                            InputTransformations.minMaxInputTransformation(0, 200),
                        ),
                    ),
                isError = maxRecruitTextStateErrorMessage != null,
                errorMessage = maxRecruitTextStateErrorMessage,
            )
        }
        SsaviceDateSpinner(
            deadline.timeInMillis,
            onDateSelected = { onDeadlineChanged(TimeStamp(it)) },
            modifier = Modifier.fillMaxWidth(),
            labelText = DEADLINE_TEXT,
            isError = deadlineErrorMessage != null,
            errorMessage = deadlineErrorMessage,
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
                enabled = enabled
            )
        }
    }
}

@Preview
@Composable
private fun AddServiceScreenPreview() {
    val categories = listOf("건강 / 생활", "식품", "취미", "스포츠", "요양", "문화")
    val state by remember {
        mutableStateOf(
            AddServiceUiState(
                form = Form(
                    name = "요가 레슨",
                    category = categories.first(),
                    tag = "요가, 필라테스, 운동",
                    minRecruit = 0,
                    maxRecruit = 0,
                    price = 0,
                    discountRatio = 0,
                    description = "",
                    startDate = TimeStamp(0L),
                    endDate = TimeStamp(0L),
                    deadline = TimeStamp(0L),
                ),
                imageState = ImageState(
                    pictureList = listOf(),
                ),
                submitState = SubmitState.Idle,
            ),
        )
    }

    SsaviceTheme {
        Scaffold { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
            ) {
                AddServiceScreen(
                    state = state,
                )
            }

        }
    }
}

internal object AddServiceScreenDefaults {
    const val ADD_IMAGE_TEXT = "사진 등록"
    const val BASIC_INFORMATION_TEXT = "기본 정보"
    const val PRICE_INFORMATION_TEXT = "비용 정보"
    const val SCHEDULE_INFORMATION_TEXT = "모집 정보"

    const val HEADER_TEXT = "새 서비스 등록"

    const val SERVICE_NAME_TEXT = "서비스명"
    const val CATEGORY_TEXT = "카테고리"
    const val TAG_TEXT = "태그 (쉽표로 구분)"
    const val MIN_RECRUIT_TEXT = "최소 모집 인원"
    const val MAX_RECRUIT_TEXT = "최대 모집 인원"
    const val PRICE_TEXT = "가격 (원)"
    const val DISCOUNT_RATIO_TEXT = "할인율"
    const val DISCOUNTED_PRICE_TEXT = "할인가"
    const val DESCRIPTION_TEXT = "서비스 설명"
    const val START_DATE_TEXT = "시작일"
    const val END_DATE_TEXT = "종료일"
    const val DEADLINE_TEXT = "모집 마감일"

    const val SERVICE_NAME_PLACEHOLDER = "예: 요가 레슨"
    const val TAG_PLACEHOLDER = "예: 요가, 필라테스, 운동"
    const val DESCRIPTION_PLACEHOLDER = "상세한 설명을 작성해주세요."
}
