package com.ssavice.seller_register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.ui.ProgressBar

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val clickable = state !is SellerRegisterUiState.Loading
    Column(
        modifier = modifier.padding(horizontal = 5.dp),
    ) {
        ProgressBar(
            maxStep = 3,
            currentStep = state.registrationStep,
            Modifier.fillMaxWidth(),
            showStep = true,
        )

        val errors = (state as? SellerRegisterUiState.Error)?.errorField

        Spacer(Modifier.height(30.dp))
        when (state.registrationStep) {
            1 -> {
                FirstPage(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                    viewModel.sellerNameState,
                    viewModel.businessRegistrationNumberState,
                    viewModel.telState,
                    errors?.contains(0) ?: false,
                    errors?.contains(1) ?: false,
                    errors?.contains(2) ?: false,
                )
            }

            2 -> {
                SecondPage(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                    addressState = viewModel.addressState,
                    descriptionState = viewModel.descriptionState,
                    errors?.contains(0) ?: false,
                )
            }

            3 -> {
                ThirdPage(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                    accountOwnerState = viewModel.accountOwnerState,
                    accountNumberState = viewModel.accountNumberState,
                    errors?.contains(0) ?: false,
                    errors?.contains(1) ?: false,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            when (state.registrationStep) {
                1 -> {
                    SsaviceButton(
                        onClick = viewModel::clickNextButton,
                        text = RegisterScreenDefaults.BUTTON_NEXT_TEXT,
                        modifier = Modifier.weight(1f),
                        enabled = clickable,
                    )
                }

                2 -> {
                    SsaviceButtonOutlined(
                        onClick = viewModel::clickPrevButton,
                        text = RegisterScreenDefaults.BUTTON_PREV_TEXT,
                        modifier = Modifier.weight(1f),
                        enabled = clickable,
                    )
                    SsaviceButton(
                        onClick = viewModel::clickNextButton,
                        text = RegisterScreenDefaults.BUTTON_NEXT_TEXT,
                        modifier = Modifier.weight(1f),
                        enabled = clickable,
                    )
                }

                3 -> {
                    SsaviceButtonOutlined(
                        onClick = viewModel::clickPrevButton,
                        text = RegisterScreenDefaults.BUTTON_PREV_TEXT,
                        modifier = Modifier.weight(1f),
                        enabled = clickable,
                    )
                    SsaviceButton(
                        onClick = viewModel::clickNextButton,
                        text = RegisterScreenDefaults.BUTTON_COMPLETE_TEXT,
                        modifier = Modifier.weight(1f),
                        enabled = clickable,
                    )
                }
            }
        }
    }
}

@Composable
fun FirstPage(
    modifier: Modifier = Modifier,
    sellerNameState: TextFieldState,
    businessRegistrationNumberState: TextFieldState,
    telState: TextFieldState,
    sellerNameError: Boolean,
    businessRegistrationNumberError: Boolean,
    telError: Boolean,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Text(
                RegisterScreenDefaults.LABEL_FIRST_PAGE,
                fontWeight = FontWeight.Bold,
            )
        }

        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = sellerNameState,
            placeholderText = RegisterScreenDefaults.SELLER_NAME_PLACEHOLDER,
            labelText = RegisterScreenDefaults.SELLER_NAME_TEXT,
            isError = sellerNameError,
            errorMessage = if (sellerNameError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = businessRegistrationNumberState,
            placeholderText = RegisterScreenDefaults.SELLER_BUSINESS_REGISTRATION_NUMBER_PLACEHOLDER,
            labelText = RegisterScreenDefaults.SELLER_BUSINESS_REGISTRATION_NUMBER_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation =
                InputTransformation.maxLength(10).then(
                    InputTransformations.digitOnlyInputTransformation,
                ),
            outputTransformation = OutputTransformations.formatBusinessNumber,
            isError = businessRegistrationNumberError,
            errorMessage = if (businessRegistrationNumberError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = telState,
            placeholderText = RegisterScreenDefaults.SELLER_TEL_PLACEHOLDER,
            labelText = RegisterScreenDefaults.SELLER_TEL_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation = InputTransformations.phoneNumberInputTransformation(),
            outputTransformation = OutputTransformations.formatPhoneNumber,
            isError = telError,
            errorMessage = if (telError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
    }
}

@Composable
fun SecondPage(
    modifier: Modifier = Modifier,
    addressState: TextFieldState,
    descriptionState: TextFieldState,
    addressError: Boolean,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Text(
                RegisterScreenDefaults.LABEL_SECOND_PAGE,
                fontWeight = FontWeight.Bold,
            )
        }

        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = addressState,
            placeholderText = RegisterScreenDefaults.ADDRESS_PLACEHOLDER,
            labelText = RegisterScreenDefaults.ADDRESS_TEXT,
            isError = addressError,
            errorMessage = if (addressError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = descriptionState,
            multiLine = true,
            placeholderText = RegisterScreenDefaults.DESCRIPTION_PLACEHOLDER,
            labelText = RegisterScreenDefaults.DESCRIPTION_TEXT,
        )
    }
}

@Composable
fun ThirdPage(
    modifier: Modifier = Modifier,
    accountOwnerState: TextFieldState,
    accountNumberState: TextFieldState,
    accountOwnerError: Boolean,
    accountNumberError: Boolean,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Text(
                RegisterScreenDefaults.LABEL_THIRD_PAGE,
                fontWeight = FontWeight.Bold,
            )
        }

        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = accountOwnerState,
            placeholderText = RegisterScreenDefaults.ACCOUNT_NAME_PLACEHOLDER,
            labelText = RegisterScreenDefaults.ACCOUNT_NAME_TEXT,
            isError = accountOwnerError,
            errorMessage = if (accountOwnerError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = accountNumberState,
            placeholderText = RegisterScreenDefaults.ACCOUNT_NUMBER_PLACEHOLDER,
            labelText = RegisterScreenDefaults.ACCOUNT_NUMBER_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation = InputTransformations.digitOnlyInputTransformation,
            isError = accountNumberError,
            errorMessage = if (accountNumberError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    SsaviceTheme {
        SsaviceBackground {
            RegisterScreen(
                modifier = Modifier.size(480.dp, 720.dp),
            )
        }
    }
}

internal object RegisterScreenDefaults {
    const val LABEL_FIRST_PAGE = "기본 정보"
    const val SELLER_NAME_TEXT = "업체명"
    const val SELLER_BUSINESS_REGISTRATION_NUMBER_TEXT = "사업자번호"
    const val SELLER_TEL_TEXT = "전화번호"

    const val LABEL_SECOND_PAGE = "위치 정보"
    const val ADDRESS_TEXT = "주소"
    const val DESCRIPTION_TEXT = "소개글"

    const val LABEL_THIRD_PAGE = "정산 정보"
    const val ACCOUNT_NAME_TEXT = "예금주"
    const val ACCOUNT_NUMBER_TEXT = "계좌번호"

    const val SELLER_NAME_PLACEHOLDER = "예: 주식회사 싸비스"
    const val SELLER_BUSINESS_REGISTRATION_NUMBER_PLACEHOLDER = "123-45-67890"
    const val SELLER_TEL_PLACEHOLDER = "010-1234-5678"
    const val ADDRESS_PLACEHOLDER = "서울특별시 강남구 ..."
    const val DESCRIPTION_PLACEHOLDER = "업체에 대해 소개해주세요"
    const val ACCOUNT_NAME_PLACEHOLDER = "홍길동"
    const val ACCOUNT_NUMBER_PLACEHOLDER = "123-456-789012"

    const val FIELD_ERROR_MESSAGE = "해당 필드는 필수입니다."

    const val BUTTON_NEXT_TEXT = "다음"
    const val BUTTON_PREV_TEXT = "이전"
    const val BUTTON_COMPLETE_TEXT = "완료"
}
