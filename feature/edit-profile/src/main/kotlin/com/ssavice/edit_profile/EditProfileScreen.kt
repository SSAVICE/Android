package com.ssavice.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssavice.designsystem.component.InputTransformations
import com.ssavice.designsystem.component.OutputTransformations
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.component.SsaviceButtonOutlined
import com.ssavice.designsystem.component.SsaviceElevatedCard
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel,
    onBackClick: () -> Unit = {},
    onSubmit: () -> Unit = {},
    onProfileImageClick: () -> Unit = {}
) {
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

    EditProfileScreen(
        modifier = modifier,
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onProfileImageClick = onProfileImageClick,
        onBackClick = onBackClick,
        onSubmitButtonClick = viewModel::onUpdateButtonClick
    )
}

private fun isStateModifiable(state: ProfileState): Boolean =
    (state is ProfileState.Error || state is ProfileState.Idle)

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
) {
    val nameState = rememberTextFieldState(state.form.name)
    val emailState = rememberTextFieldState(state.form.email)
    val phoneNumberState = rememberTextFieldState(state.form.phoneNumber)

    LaunchedEffect(state.form) {
        if(state.form.name != nameState.text.toString()) {
            nameState.edit {
                replace(0, nameState.text.length, state.form.name)
            }
        }
        if(state.form.email != emailState.text.toString()) {
            emailState.edit {
                replace(0, emailState.text.length, state.form.email)
            }
        }
        if(state.form.phoneNumber != phoneNumberState.text.toString()) {
            phoneNumberState.edit {
                replace(0, phoneNumberState.text.length, state.form.phoneNumber)
            }
        }
    }

    LaunchedEffect(nameState) {
        if(isStateModifiable(state.profileUpdateState)) {
            snapshotFlow { nameState.text.toString() }
                .collectLatest { onNameChange(it) }
        }
    }

    LaunchedEffect(emailState) {
        if(isStateModifiable(state.profileUpdateState)) {
            snapshotFlow { emailState.text.toString() }
                .collectLatest { onEmailChange(it) }
        }
    }

    LaunchedEffect(phoneNumberState) {
        if(isStateModifiable(state.profileUpdateState)) {
            snapshotFlow { phoneNumberState.text.toString() }
                .collectLatest { onPhoneNumberChange(it) }
        }
    }


    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Profile Image Card
        SsaviceElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = if (isStateModifiable(state.imageUpdateState)) onProfileImageClick else null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    when (state.profileImage) {
                        is EditProfileImage.UrlImage -> {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(state.profileImage.url)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        is EditProfileImage.BitmapImage -> {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(state.profileImage.bitmap)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        else -> {}
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FileUpload,
                        contentDescription = "Upload",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "사진 변경",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        val enabled = isStateModifiable(state.profileUpdateState)

        // Basic Information Card
        SsaviceElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "기본 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                SsaviceInputField(
                    state = nameState,
                    labelText = "이름",
                    placeholderText = "이름을 입력해주세요",
                    isError = state.form.nameErrorMessage != null,
                    errorMessage = state.form.nameErrorMessage,
                    enabled = enabled
                )

                SsaviceInputField(
                    state = emailState,
                    labelText = "이메일",
                    placeholderText = "이메일을 입력해주세요",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = state.form.emailErrorMessage != null,
                    errorMessage = state.form.emailErrorMessage,
                    enabled = enabled
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
                    enabled = enabled
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            SsaviceButtonOutlined(
                modifier = Modifier.weight(1f),
                text = "취소",
                onClick = onBackClick
            )
            SsaviceButton(
                modifier = Modifier.weight(1f),
                text = "저장",
                onClick = onSubmitButtonClick,
                enabled = enabled && isStateModifiable(state.profileUpdateState)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    val state = EditProfileState(
        form = EditProfileForm(
            name = "김민준",
            email = "mingjun.kim@example.com",
            phoneNumber = "01012341234",
        ),
        profileImage = EditProfileImage.UrlImage("https://picsum.photos/200"),
        profileUpdateState = ProfileState.Idle,
        imageUpdateState = ProfileState.Idle
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
                onSubmitButtonClick = {}
            )
        }
    }
}
