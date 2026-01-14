package com.ssavice.edit_profile

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
import com.ssavice.designsystem.component.OutputTransformations
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.component.SsaviceButtonOutlined
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.ui.uploadImage.ImageWithUploadState
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

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.onUserProfileImageSelected(uri)
            }
        }
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.profileUpdateState) {
        when (state.profileUpdateState) {
            ProfileState.Done -> {
                onSubmit()
            }

            is ProfileState.Error -> {}

            ProfileState.Fetching -> {}

            ProfileState.Idle -> {}

            ProfileState.Initial -> {
                viewModel.initUiState()
            }

            ProfileState.Updating -> {}
        }
    }
    val profileImageClick = if(isStateModifiable(state.imageUpdateState)) {
        {
            photoPickerLauncher
                .launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }
    else {
        {}
    }

    EditProfileScreen(
        modifier = modifier,
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onProfileImageClick = profileImageClick,
        onBackClick = onBackClick,
        onSubmitButtonClick = viewModel::onUpdateButtonClick,
        imageUploading = state.imageUpdateState is ProfileState.Updating
    )
}

private fun isStateModifiable(state: ProfileState): Boolean = (state is ProfileState.Error || state is ProfileState.Idle)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    state: EditProfileState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onProfileImageClick: () -> Unit,
    onBackClick: () -> Unit,
    onSubmitButtonClick: () -> Unit,
    imageUploading: Boolean = false
) {
    val dataInitialized = (state.profileUpdateState != ProfileState.Initial && state.form.name.isNotEmpty() || isStateModifiable(state.profileUpdateState))
    val nameState = rememberTextFieldState(state.form.name)
    val emailState = rememberTextFieldState(state.form.email)
    val phoneNumberState = rememberTextFieldState(state.form.phoneNumber)

    LaunchedEffect(dataInitialized) {
        if(dataInitialized) {
            nameState.edit {
                replace(0, nameState.text.length, state.form.name)
            }
            emailState.edit {
                replace(0, emailState.text.length, state.form.email)
            }
            phoneNumberState.edit {
                replace(0, phoneNumberState.text.length, state.form.phoneNumber)
            }
        }
    }

    if(dataInitialized) {
        LaunchedEffect(nameState) {
            snapshotFlow { nameState.text }
                .collectLatest { onNameChange(it.toString()) }
        }

        LaunchedEffect(emailState) {
            snapshotFlow { emailState.text }
                .collectLatest { onEmailChange(it.toString()) }
        }

        LaunchedEffect(phoneNumberState) {
            snapshotFlow { phoneNumberState.text }
                .collectLatest { onPhoneNumberChange(it.toString()) }
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
                    labelText = "이름",
                    placeholderText = "이름을 입력해주세요",
                    isError = state.form.nameErrorMessage != null,
                    errorMessage = state.form.nameErrorMessage,
                    enabled = enabled,
                )

                SsaviceInputField(
                    state = emailState,
                    labelText = "이메일",
                    placeholderText = "이메일을 입력해주세요",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = state.form.emailErrorMessage != null,
                    errorMessage = state.form.emailErrorMessage,
                    enabled = enabled,
                )

                SsaviceInputField(
                    state = phoneNumberState,
                    labelText = "전화번호",
                    placeholderText = "전화번호를 입력해주세요",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    inputTransformation = InputTransformations.digitOnlyInputTransformation,
                    outputTransformation = OutputTransformations.formatPhoneNumber,
                    isError = state.form.phoneNumberErrorMessage != null,
                    errorMessage = state.form.phoneNumberErrorMessage,
                    enabled = enabled,
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
                enabled = enabled && isStateModifiable(state.imageUpdateState),
            )
        }
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
                    email = "mingjun.kim@example.com",
                    phoneNumber = "01012341234",
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
                onEmailChange = {},
                onPhoneNumberChange = {},
                onProfileImageClick = {},
                onBackClick = {},
                onSubmitButtonClick = {},
            )
        }
    }
}
