package com.ssavice.seller_edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.component.InputTransformations
import com.ssavice.designsystem.component.LabeledComponent
import com.ssavice.designsystem.component.OutputTransformations
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.component.SsaviceButtonOutlined
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.edit_profile.EditProfileViewModel
import com.ssavice.mappicker.AddressPickerDialog
import com.ssavice.mappicker.AddressPickerSelectButton
import com.ssavice.ui.common.Constant
import com.ssavice.ui.uploadImage.ImageWithUploadState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel,
    onBackClick: () -> Unit = {},
    onSubmit: () -> Unit = {},
    onProfileImageClick: () -> Unit = {},
) {
    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                if (uri != null) {
                    viewModel.onUserProfileImageSelected(uri)
                }
            },
        )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.profileUpdateState) {
        when (state.profileUpdateState) {
            ProfileState.Done -> {
                onSubmit()
            }

            ProfileState.Initial -> {
                delay(Constant.ANIMATION_DELAY)
                viewModel.initUiState()
            }

            else -> {}
        }
    }
    LaunchedEffect(state.addressState) {
        if (state.addressState is AddressFormState.Initial) {
            delay(Constant.ANIMATION_DELAY)
            viewModel.initAddressState()
        }
    }

    val profileImageClick =
        if (isStateModifiable(state.imageUpdateState)) {
            {
                photoPickerLauncher
                    .launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
            }
        } else {
            {}
        }

    EditProfileScreen(
        modifier = modifier,
        state = state,
        onNameChange = viewModel::onNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onDetailChange = viewModel::onDetailChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onProfileImageClick = profileImageClick,
        onDetailAddressChange = viewModel::onDetailAddressChange,
        onAddressUpdate = viewModel::onAddressUpdate,
        onBackClick = onBackClick,
        onSubmitButtonClick = viewModel::onUpdateButtonClick,
        imageUploading = state.imageUpdateState is ProfileState.Updating,
    )
}

private fun isStateModifiable(state: ProfileState): Boolean = (state is ProfileState.Error || state is ProfileState.Idle)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    state: EditProfileState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDetailChange: (String) -> Unit,
    onProfileImageClick: () -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onDetailAddressChange: (String) -> Unit,
    onAddressUpdate: (AddressState) -> Unit,
    onBackClick: () -> Unit,
    onSubmitButtonClick: () -> Unit,
    imageUploading: Boolean = false,
) {
    val dataInitialized =
        (state.profileUpdateState != ProfileState.Initial && state.form.name.isNotEmpty()) ||
            isStateModifiable(state.profileUpdateState)
    val addressInitialized =
        state.addressState == AddressFormState.Idle &&
            state.form.address.address
                .isNotEmpty()
    val nameState = rememberTextFieldState(state.form.name)
    val descriptionState = rememberTextFieldState(state.form.description)
    val detailState = rememberTextFieldState(state.form.detail)
    val phoneNumberState = rememberTextFieldState(state.form.phoneNumber)
    val detailAddressState = rememberTextFieldState(state.form.detailAddress)

    LaunchedEffect(dataInitialized) {
        if (dataInitialized) {
            nameState.edit {
                replace(0, nameState.text.length, state.form.name)
            }
            descriptionState.edit {
                replace(0, descriptionState.text.length, state.form.description)
            }
            detailState.edit {
                replace(0, phoneNumberState.text.length, state.form.detail)
            }
            phoneNumberState.edit {
                replace(0, phoneNumberState.text.length, state.form.phoneNumber)
            }
        }
    }
    LaunchedEffect(addressInitialized) {
        if (addressInitialized) {
            detailAddressState.edit {
                replace(0, detailAddressState.text.length, state.form.detailAddress)
            }
        }
    }

    if (dataInitialized) {
        LaunchedEffect(nameState) {
            snapshotFlow { nameState.text }
                .collectLatest { onNameChange(it.toString()) }
        }

        LaunchedEffect(descriptionState) {
            snapshotFlow { descriptionState.text }
                .collectLatest { onDescriptionChange(it.toString()) }
        }

        LaunchedEffect(detailState) {
            snapshotFlow { detailState.text }
                .collectLatest { onDetailChange(it.toString()) }
        }

        LaunchedEffect(phoneNumberState) {
            snapshotFlow { phoneNumberState.text }
                .collectLatest { onPhoneNumberChange(it.toString()) }
        }
    }
    if (addressInitialized) {
        LaunchedEffect(detailAddressState) {
            snapshotFlow { detailAddressState.text }
                .collectLatest { onDetailAddressChange(it.toString()) }
        }
    }

    Column(
        modifier =
            modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Profile Image Card
        SsaviceElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = if (isStateModifiable(state.imageUpdateState)) onProfileImageClick else null,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                ) {
                    ImageWithUploadState(
                        baseImageUrl = state.profileImage,
                        uploadingImageUrl = state.imageSelectedUri?.toString(),
                        uploadState = state.imageUploadProgress,
                        contentDescription = "Profile Image",
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier =
                        Modifier
                            .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!imageUploading) {
                        Icon(
                            imageVector = Icons.Default.FileUpload,
                            contentDescription = "Upload",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "사진 변경",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "업로드 중",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }

        val enabled = isStateModifiable(state.profileUpdateState)

        // Basic Information Card
        SsaviceElevatedCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "기본 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                SsaviceInputField(
                    state = nameState,
                    labelText = "판매자명",
                    placeholderText = "",
                    isError = state.form.nameErrorMessage != null,
                    errorMessage = state.form.nameErrorMessage,
                    enabled = enabled,
                )

                SsaviceInputField(
                    state = descriptionState,
                    labelText = "판매자 소게",
                    isError = state.form.descriptionErrorMessage != null,
                    errorMessage = state.form.descriptionErrorMessage,
                    enabled = enabled,
                )

                SsaviceInputField(
                    state = detailState,
                    labelText = "상세 설명",
                    isError = state.form.detailErrorMessage != null,
                    errorMessage = state.form.detailErrorMessage,
                    multiLine = true,
                    enabled = enabled,
                )

                SsaviceInputField(
                    state = phoneNumberState,
                    labelText = "전화번호",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    inputTransformation = InputTransformations.digitOnlyInputTransformation,
                    outputTransformation = OutputTransformations.formatPhoneNumber,
                    isError = state.form.phoneNumberErrorMessage != null,
                    errorMessage = state.form.phoneNumberErrorMessage,
                    enabled = enabled,
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        SsaviceElevatedCard(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "주소 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                AddressForm(
                    addressState = state.form.address,
                    onAddressChange = onAddressUpdate,
                    detailAddressState = detailAddressState,
                    detailAddressError = state.form.detailAddressErrorMessage,
                    enabled = state.addressState is AddressFormState.Idle,
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            SsaviceButtonOutlined(
                modifier = Modifier.weight(1f),
                text = "취소",
                onClick = onBackClick,
            )
            SsaviceButton(
                modifier = Modifier.weight(1f),
                text = "저장",
                onClick = onSubmitButtonClick,
                enabled =
                    enabled &&
                        isStateModifiable(state.imageUpdateState) &&
                        state.addressState is AddressFormState.Idle,
            )
        }
    }
}

@Composable
private fun AddressForm(
    addressState: AddressState,
    onAddressChange: (AddressState) -> Unit,
    detailAddressState: TextFieldState,
    detailAddressError: String?,
    enabled: Boolean = true,
) {
    var showAddressPicker by remember { mutableStateOf(false) }

    LabeledComponent(
        Modifier,
        "주소",
    ) {
        AddressPickerSelectButton(
            addressState.address,
        ) {
            showAddressPicker = true
        }
    }

    SsaviceInputField(
        state = detailAddressState,
        labelText = "상세 주소",
        isError = detailAddressError != null,
        errorMessage = detailAddressError,
        enabled = enabled,
    )

    if (showAddressPicker) {
        AddressPickerDialog(
            onSelect = {
                it.run {
                    onAddressChange(
                        AddressState(
                            address = address,
                            latitude = latitude,
                            longitude = longitude,
                            regionCode = regionCode,
                            postCode = zipCode,
                        ),
                    )
                }
                showAddressPicker = false
            },
            onDismiss = {
                showAddressPicker = false
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    val state =
        EditProfileState(
            form =
                EditProfileForm(
                    name = "김민준",
                    phoneNumber = "01012341234",
                    description = "안녕하세요",
                    detail = "저는 김민준입니다.",
                ),
            profileImage = "https://picsum.photos/200",
            profileUpdateState = ProfileState.Idle,
            imageUpdateState = ProfileState.Idle,
        )
    SsaviceTheme {
        Scaffold { paddingValues ->
            EditProfileScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                onNameChange = {},
                onPhoneNumberChange = {},
                onProfileImageClick = {},
                onBackClick = {},
                onDescriptionChange = {},
                onDetailChange = {},
                onDetailAddressChange = {},
                onSubmitButtonClick = {},
                onAddressUpdate = {},
            )
        }
    }
}
