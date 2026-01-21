package com.ssavice.seller_register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.datastore.repository.BusinessVerificationRepository
import com.ssavice.model.Date
import com.ssavice.model.RegionInfo
import com.ssavice.model.TimeStamp
import com.ssavice.model.auth.CompanyVerifyToken
import com.ssavice.model.seller.SellerRegisterForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val repository: SellerInfoRepository,
        private val businessVerificationRepository: BusinessVerificationRepository,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow(
                SellerRegisterUiState(
                    form =
                        Form(
                            registrationStep = 1,
                            sellerName = "",
                            businessOwnerName = "",
                            businessRegistrationNumber = "",
                            tel = "",
                            detailAddress = "",
                            description = "",
                            accountDepositor = "",
                            accountNumber = "",
                            companyOpenDate = TimeStamp(0L),
                            address =
                                AddressForm(
                                    address = "",
                                    regionCode = "",
                                    latitude = 0.0,
                                    longitude = 0.0,
                                    zipCode = "",
                                ),
                        ),
                    submitState = SubmitState.Shown,
                    showTestButton = false,
                    companyValidationState = ValidationState.NotValidated,
                    tokenRemainingTime = 0L,
                ),
            )
        val uiState: StateFlow<SellerRegisterUiState> = _uiState

        private val tokenState =
            businessVerificationRepository.getToken().map {
                _uiState.update { state ->
                    if (state.companyValidationState !is ValidationState.Validating) {
                        state.copy(
                            companyValidationState = if (it.isExpired()) ValidationState.NotValidated else ValidationState.Validated,
                        )
                    } else {
                        state
                    }
                }
                it
            }

        private fun checkBusinessInfoPageAndValidate() {
            var hasError = false

            val ownerState =
                if (_uiState.value.form.businessOwnerName
                        .isEmpty()
                ) {
                    hasError = true
                    FormError.EmptyField
                } else {
                    FormError.None
                }

            val businessRegistrationNumberState =
                if (_uiState.value.form.businessRegistrationNumber.length != 10) {
                    hasError = true
                    FormError.EmptyField
                } else {
                    FormError.None
                }

            val companyOpenDateState =
                if (_uiState.value.form.companyOpenDate.timeInMillis == 0L) {
                    hasError = true
                    FormError.EmptyField
                } else {
                    FormError.None
                }

            _uiState.value =
                _uiState.value.copy(
                    form =
                        _uiState.value.form.copy(
                            businessOwnerNameErrorState = ownerState,
                            businessRegistrationNumberErrorState = businessRegistrationNumberState,
                            companyOpenDateErrorState = companyOpenDateState,
                        ),
                )

            if (!hasError) {
                requestValidationAndProceed()
            }
        }

        private fun requestValidationAndProceed() {
            _uiState.update {
                it.copy(
                    companyValidationState = ValidationState.Validating,
                )
            }

            viewModelScope.launch(Dispatchers.IO) {
                repository
                    .verifyBusinessInfo(
                        name = _uiState.value.form.businessOwnerName,
                        openDate = Date.parse(_uiState.value.form.companyOpenDate),
                        businessNumber = _uiState.value.form.businessRegistrationNumber,
                    ).fold(
                        onSuccess = { token ->
                            businessVerificationRepository.setToken(
                                token,
                            )
                            val token = tokenState.first()
                            _uiState.update {
                                it.copy(
                                    companyValidationState = ValidationState.Validated,
                                )
                            }
                        },
                        onFailure = {
                            _uiState.update {
                                it.copy(
                                    companyValidationState = ValidationState.Failed,
                                )
                            }
                        },
                    )
            }
        }

        private fun checkTokenAndProcess() {
            viewModelScope.launch {
                if (tokenState.first().isExpired()) {
                    _uiState.update {
                        it.copy(
                            companyValidationState = ValidationState.NotValidated,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            form =
                                it.form.copy(
                                    registrationStep = it.form.registrationStep + 1,
                                ),
                        )
                    }
                }
            }
        }

        private fun checkLocationInfoPageFieldsAndProcess() {
            var hasError = false

            val nameState =
                if (_uiState.value.form.sellerName
                        .isEmpty()
                ) {
                    hasError = true
                    FormError.EmptyField
                } else {
                    FormError.None
                }

            val telState =
                if (_uiState.value.form.tel.length < 10) {
                    hasError = true
                    FormError.EmptyField
                } else {
                    FormError.None
                }

            val addressState =
                if (_uiState.value.form.detailAddress
                        .isEmpty()
                ) {
                    hasError = true
                    FormError.EmptyField
                } else {
                    FormError.None
                }

            val nextStep =
                if (!hasError) _uiState.value.form.registrationStep + 1 else _uiState.value.form.registrationStep
            _uiState.value =
                _uiState.value.copy(
                    form =
                        _uiState.value.form.copy(
                            registrationStep = nextStep,
                            addressErrorState = addressState,
                            sellerNameErrorState = nameState,
                            telErrorState = telState,
                        ),
                )
        }

        private fun checkAccountInfoPageFieldsAndProcess() {
            var hasError = false

            val accountOwnerState =
                if (_uiState.value.form.accountDepositor
                        .isEmpty()
                ) {
                    hasError = true
                    FormError.EmptyField
                } else {
                    FormError.None
                }
            val accountNumberState =
                if (_uiState.value.form.accountNumber
                        .isEmpty()
                ) {
                    hasError = true
                    FormError.EmptyField
                } else {
                    FormError.None
                }
            _uiState.value =
                _uiState.value.copy(
                    form =
                        _uiState.value.form.copy(
                            accountDepositorErrorState = accountOwnerState,
                            accountNumberErrorState = accountNumberState,
                        ),
                )

            if (!hasError) {
                submit()
            }
        }

        fun onAddressSelected(address: AddressForm) {
            _uiState.update {
                it.copy(
                    form =
                        it.form.copy(
                            address = address,
                        ),
                )
            }
        }

        fun updateTokenInfo() {
            viewModelScope.launch {
                val token = tokenState.first()
                Log.d(TAG, "updateTokenInfo: $token")

                if (uiState.value.companyValidationState != ValidationState.Validating) {
                    _uiState.update {
                        it.copy(
                            companyValidationState = if (token.isExpired()) ValidationState.NotValidated else ValidationState.Validated,
                        )
                    }
                }
                if (uiState.value.companyValidationState == ValidationState.Validated) {
                    val remainingTime = (token.createdAt + CompanyVerifyToken.EXPIRATION_TIME - System.currentTimeMillis()) / 1000

                    if (remainingTime != uiState.value.tokenRemainingTime) {
                        _uiState.update {
                            it.copy(
                                tokenRemainingTime = remainingTime,
                            )
                        }
                    }
                    if (remainingTime <= 0 && uiState.value.form.registrationStep > 1) {
                        clearToken()
                        _uiState.update {
                            it.copy(
                                form = it.form.copy(registrationStep = 1),
                            )
                        }
                    }
                }
            }
        }

        fun clearToken() {
            viewModelScope.launch {
                businessVerificationRepository.clearToken()
            }
        }

        fun onValidateButtonClick() {
            checkBusinessInfoPageAndValidate()
        }

        fun clickNextButton() {
            viewModelScope.launch {
                when (_uiState.value.form.registrationStep) {
                    1 -> checkTokenAndProcess()
                    2 -> checkLocationInfoPageFieldsAndProcess()
                    3 -> checkAccountInfoPageFieldsAndProcess()
                }
            }
        }

        fun clickPrevButton() {
            if (_uiState.value.form.registrationStep == 1) {
                clearToken()
                return
            }
            viewModelScope.launch {
                _uiState.value =
                    _uiState.value.copy(
                        form =
                            _uiState.value.form.copy(
                                registrationStep = max(_uiState.value.form.registrationStep - 1, 1),
                            ),
                    )
            }
        }

        fun submit() {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.value =
                    _uiState.value.copy(
                        submitState = SubmitState.Loading,
                    )
                repository
                    .registerSellerInformation(
                        SellerRegisterForm(
                            companyName = _uiState.value.form.sellerName,
                            businessNumber = _uiState.value.form.businessRegistrationNumber,
                            phoneNumber = _uiState.value.form.tel,
                            description = _uiState.value.form.description,
                            detail = _uiState.value.form.description,
                            businessOwnerName = _uiState.value.form.businessOwnerName,
                            accountNumber = _uiState.value.form.accountNumber,
                            accountDepositor = _uiState.value.form.accountDepositor,
                            region =
                                RegionInfo(
                                    address = _uiState.value.form.address.address,
                                    detailAddress = _uiState.value.form.detailAddress,
                                    postCode = _uiState.value.form.address.zipCode,
                                    latitude = _uiState.value.form.address.latitude,
                                    longitude = _uiState.value.form.address.longitude,
                                    regionCode = _uiState.value.form.address.regionCode,
                                ),
                        ),
                        tokenState.first(),
                    ).fold(
                        onSuccess = {
                            _uiState.value =
                                _uiState.value.copy(
                                    submitState = SubmitState.Submit,
                                )
                        },
                    ) {
                        _uiState.value =
                            _uiState.value.copy(
                                submitState = SubmitState.Error(it.message ?: ""),
                            )
                    }
            }
        }

        fun onCompanyOpenDateChanged(date: TimeStamp) {
            _uiState.update {
                it.copy(
                    form =
                        it.form.copy(
                            companyOpenDate = date,
                        ),
                )
            }
        }

        fun onFirstPageFormChanged(
            sellerName: String,
            businessOwnerName: String,
            businessRegistrationNumber: String,
            tel: String,
        ) {
            _uiState.value =
                _uiState.value.copy(
                    form =
                        _uiState.value.form.copy(
                            sellerName = sellerName,
                            businessOwnerName = businessOwnerName,
                            businessRegistrationNumber = businessRegistrationNumber,
                            tel = tel,
                        ),
                )
        }

        fun onSecondPageFormChanged(
            address: String,
            description: String,
        ) {
            _uiState.value =
                _uiState.value.copy(
                    form =
                        _uiState.value.form.copy(
                            detailAddress = address,
                            description = description,
                        ),
                )
        }

        fun onThirdPageFormChanged(
            accountOwner: String,
            accountNumber: String,
        ) {
            _uiState.value =
                _uiState.value.copy(
                    form =
                        _uiState.value.form.copy(
                            accountDepositor = accountOwner,
                            accountNumber = accountNumber,
                        ),
                )
        }

        companion object {
            private const val TAG = "RegisterViewModel"
        }
    }
