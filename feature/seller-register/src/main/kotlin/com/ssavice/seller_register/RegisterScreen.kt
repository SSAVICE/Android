package com.ssavice.seller_register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.common.Quadruple
import com.ssavice.designsystem.component.SsaviceBackground
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.component.SsaviceButtonOutlined
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.TimeStamp
import com.ssavice.seller_register.navigation.SellerRegisterNavHost
import com.ssavice.ui.ProgressBar
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val clickable: Boolean =
        if (state.form.registrationStep == 1) {
            (
                (state.submitState !is SubmitState.Loading) &&
                    (state.companyValidationState == ValidationState.Validated)
            )
        } else {
            state.submitState !is SubmitState.Loading
        }

    val sellerNameState = rememberTextFieldState(state.form.sellerName)
    val businessOwnerState = rememberTextFieldState(state.form.businessOwnerName)
    val businessNameState = rememberTextFieldState(state.form.businessName)
    val businessRegistrationNumberState =
        rememberTextFieldState(state.form.businessRegistrationNumber)
    val telState = rememberTextFieldState(state.form.tel)
    val addressState = rememberTextFieldState(state.form.detailAddress)
    val descriptionState = rememberTextFieldState(state.form.description)
    val accountOwnerState = rememberTextFieldState(state.form.accountDepositor)
    val accountNumberState = rememberTextFieldState(state.form.accountNumber)

    LaunchedEffect(state.submitState is SubmitState.Submit) {
        if (state.submitState is SubmitState.Submit) {
            onSubmit.invoke()
        }
    }

    LaunchedEffect(sellerNameState, businessRegistrationNumberState, telState) {
        snapshotFlow {
            Quadruple(
                businessOwnerState.text.toString(),
                businessNameState.text.toString(),
                businessRegistrationNumberState.text.toString(),
                telState.text.toString(),
            )
        }.collect {
            viewModel.onFirstPageFormChanged(
                it.first,
                it.second,
                it.third,
                it.fourth,
            )
        }
    }

    LaunchedEffect(addressState, descriptionState) {
        snapshotFlow {
            Triple(
                sellerNameState.text.toString(),
                addressState.text.toString(),
                descriptionState.text.toString(),
            )
        }.collect {
            viewModel.onSecondPageFormChanged(
                it.first,
                it.second,
                it.third,
            )
        }
    }

    LaunchedEffect(accountOwnerState, accountNumberState) {
        snapshotFlow {
            Pair(
                accountOwnerState.text.toString(),
                accountNumberState.text.toString(),
            )
        }.collect {
            viewModel.onThirdPageFormChanged(
                it.first,
                it.second,
            )
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.updateTokenInfo()

            delay(500L)
        }
    }

    RegisterScreen(
        modifier = modifier,
        onNextButtonClick = viewModel::clickNextButton,
        onPrevButtonClick = viewModel::clickPrevButton,
        active = clickable,
        page = state.form.registrationStep,
        sellerNameState = sellerNameState,
        businessOwnerState = businessOwnerState,
        businessNameState = businessNameState,
        businessRegistrationNumberState = businessRegistrationNumberState,
        companyOpenDate = state.form.companyOpenDate,
        companyValidationState = state.companyValidationState,
        onCompanyOpenDateChanged = viewModel::onCompanyOpenDateChanged,
        onValidateButtonClicked = viewModel::onValidateButtonClick,
        telState = telState,
        detailAddressState = addressState,
        addressState = state.form.address,
        onAddressSelected = viewModel::onAddressSelected,
        descriptionState = descriptionState,
        accountOwnerState = accountOwnerState,
        accountNumberState = accountNumberState,
        sellerNameErrorState = state.form.sellerNameErrorState,
        businessOwnerErrorState = state.form.businessOwnerNameErrorState,
        businessNameErrorState = state.form.businessNameErrorState,
        businessRegistrationNumberErrorState = state.form.businessRegistrationNumberErrorState,
        companyOpenDateErrorState = state.form.companyOpenDateErrorState,
        telErrorState = state.form.telErrorState,
        addressErrorState = state.form.addressErrorState,
        accountOwnerErrorState = state.form.accountDepositorErrorState,
        accountNumberErrorState = state.form.accountNumberErrorState,
        submitButtonForTest = if (state.showTestButton) viewModel::submit else null,
        tokenRemainingTime = state.tokenRemainingTime,
    )
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onNextButtonClick: () -> Unit = {},
    onPrevButtonClick: () -> Unit = {},
    active: Boolean,
    page: Int,
    sellerNameState: TextFieldState,
    businessOwnerState: TextFieldState,
    businessNameState: TextFieldState,
    businessRegistrationNumberState: TextFieldState,
    companyOpenDate: TimeStamp,
    onCompanyOpenDateChanged: (TimeStamp) -> Unit,
    onValidateButtonClicked: () -> Unit,
    onAddressSelected: (AddressForm) -> Unit,
    telState: TextFieldState,
    addressState: AddressForm,
    detailAddressState: TextFieldState,
    descriptionState: TextFieldState,
    accountOwnerState: TextFieldState,
    accountNumberState: TextFieldState,
    companyValidationState: ValidationState,
    sellerNameErrorState: FormError,
    companyOpenDateErrorState: FormError,
    businessOwnerErrorState: FormError,
    businessNameErrorState: FormError,
    businessRegistrationNumberErrorState: FormError,
    telErrorState: FormError,
    addressErrorState: FormError,
    accountOwnerErrorState: FormError,
    accountNumberErrorState: FormError,
    submitButtonForTest: (() -> Unit)? = null,
    tokenRemainingTime: Long,
) {
    Column(
        modifier = modifier.padding(horizontal = 5.dp),
    ) {
        ProgressBar(
            maxStep = 3,
            currentStep = page,
            Modifier.fillMaxWidth(),
            showStep = true,
        )

        val showPrevButton = (page != 1)
        val nextButtonText =
            if (page == 3) RegisterScreenDefaults.BUTTON_COMPLETE_TEXT else RegisterScreenDefaults.BUTTON_NEXT_TEXT

        Spacer(Modifier.height(30.dp))
        SellerRegisterNavHost(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            page = page,
            sellerNameState = sellerNameState,
            businessOwnerState = businessOwnerState,
            businessNameState = businessNameState,
            businessRegistrationNumberState = businessRegistrationNumberState,
            telState = telState,
            companyValidationState = companyValidationState,
            sellerNameError = sellerNameErrorState != FormError.None,
            businessOwnerError = businessOwnerErrorState != FormError.None,
            businessNameError = businessNameErrorState != FormError.None,
            businessRegistrationNumberError = businessRegistrationNumberErrorState != FormError.None,
            telError = telErrorState != FormError.None,
            detailAddressState = detailAddressState,
            addressState = addressState,
            descriptionState = descriptionState,
            addressError = addressErrorState != FormError.None,
            accountDepositorState = accountOwnerState,
            accountNumberState = accountNumberState,
            accountDepositorError = accountOwnerErrorState != FormError.None,
            accountNumberError = accountNumberErrorState != FormError.None,
            onCompanyOpenDateChanged = onCompanyOpenDateChanged,
            onValidateButtonClicked = onValidateButtonClicked,
            companyOpenDate = companyOpenDate,
            companyOpenDateError = companyOpenDateErrorState != FormError.None,
            tokenRemainingTime = tokenRemainingTime,
            onAddressSelected = onAddressSelected,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            SsaviceButtonOutlined(
                onClick = onPrevButtonClick,
                text = if (page == 1) RegisterScreenDefaults.BUTTON_RESET else RegisterScreenDefaults.BUTTON_PREV_TEXT,
                modifier = Modifier.weight(1f),
                enabled = active,
            )
            SsaviceButton(
                onClick = onNextButtonClick,
                text = nextButtonText,
                modifier = Modifier.weight(1f),
                enabled = active,
            )
            if (submitButtonForTest != null) {
                SsaviceButton(
                    onClick = submitButtonForTest,
                    text = "TEST",
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    SsaviceTheme {
        SsaviceBackground {
            RegisterScreen(
                modifier = Modifier.size(480.dp, 720.dp),
                {},
            )
        }
    }
}

internal object RegisterScreenDefaults {
    const val LABEL_FIRST_PAGE = "사업자 정보"
    const val BUSINESS_OWNER_TEXT = "대표자명"
    const val OPEN_DATE_TEXT = "개업 일자"
    const val SELLER_BUSINESS_REGISTRATION_NUMBER_TEXT = "사업자번호"

    const val LABEL_SECOND_PAGE = "기본 정보"
    const val SELLER_NAME_TEXT = "이름"
    const val BUSINESS_NAME_TEXT = "상호명"
    const val ADDRESS_TEXT = "주소"
    const val DETAIL_ADDRESS_TEXT = "상세 주소"
    const val SELLER_TEL_TEXT = "전화번호"
    const val DESCRIPTION_TEXT = "소개글"

    const val LABEL_THIRD_PAGE = "정산 정보"
    const val ACCOUNT_NAME_TEXT = "예금주"
    const val ACCOUNT_NUMBER_TEXT = "계좌번호"

    const val SELLER_NAME_PLACEHOLDER = "예: 싸비스"
    const val BUSINESS_NAME_PLACEHOLDER = "예: 주식회사 싸비스"
    const val BUSINESS_OWNER_PLACEHOLDER = "예: 권성찬"
    const val SELLER_BUSINESS_REGISTRATION_NUMBER_PLACEHOLDER = "123-45-67890"
    const val SELLER_TEL_PLACEHOLDER = "010-1234-5678"
    const val ADDRESS_PLACEHOLDER = "주소를 입력하세요"
    const val ADDRESS_DETAIL_PLACEHOLDER = "101동 1001호"
    const val DESCRIPTION_PLACEHOLDER = "업체에 대해 소개해주세요"
    const val ACCOUNT_NAME_PLACEHOLDER = "홍길동"
    const val ACCOUNT_NUMBER_PLACEHOLDER = "123-456-789012"

    const val FIELD_ERROR_MESSAGE = "해당 필드는 필수입니다."

    const val VALIDATE_BUTTON = "사업자 정보 검증"
    const val VALIDATE_BUTTON_COMPLETE = "검증 완료"
    const val BUTTON_NEXT_TEXT = "다음"
    const val BUTTON_PREV_TEXT = "이전"
    const val BUTTON_RESET = "초기화"
    const val BUTTON_COMPLETE_TEXT = "완료"
}
