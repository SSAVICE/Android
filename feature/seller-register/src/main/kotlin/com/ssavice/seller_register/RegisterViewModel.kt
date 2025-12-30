package com.ssavice.seller_register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.RegionInfo
import com.ssavice.model.seller.SellerRegisterForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val repository: SellerInfoRepository,
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
                            address = "",
                            description = "",
                            accountDepositor = "",
                            accountNumber = "",
                        ),
                    submitState = SubmitState.Shown,
                ),
            )
        val uiState: StateFlow<SellerRegisterUiState> = _uiState

        private fun checkBasicInfoPageFieldsAndProcess() {
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
            val telState =
                if (_uiState.value.form.tel.length < 10) {
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
                            sellerNameErrorState = nameState,
                            businessOwnerNameErrorState = ownerState,
                            businessRegistrationNumberErrorState = businessRegistrationNumberState,
                            telErrorState = telState,
                        ),
                )
        }

        private fun checkLocationInfoPageFieldsAndProcess() {
            var hasError = false

            val addressState =
                if (_uiState.value.form.address
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

        fun clickNextButton() {
            viewModelScope.launch {
                when (_uiState.value.form.registrationStep) {
                    1 -> checkBasicInfoPageFieldsAndProcess()
                    2 -> checkLocationInfoPageFieldsAndProcess()
                    3 -> checkAccountInfoPageFieldsAndProcess()
                }
            }
        }

        fun clickPrevButton() {
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
                            detail = "",
                            businessOwnerName = _uiState.value.form.businessOwnerName,
                            accountNumber = _uiState.value.form.accountNumber,
                            accountDepositor = _uiState.value.form.accountDepositor,
                            region = RegionInfo.demo
                        ),
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
                            address = address,
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
    }
