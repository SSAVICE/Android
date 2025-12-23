package com.ssavice.seller_register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.SellerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class RegisterViewModel
    @Inject
    constructor(
        private val repository: SellerInfoRepository,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow<SellerRegisterUiState>(
                SellerRegisterUiState.Shown(1),
            )
        val uiState: StateFlow<SellerRegisterUiState> = _uiState

        val sellerNameState = TextFieldState("")
        val businessRegistrationNumberState = TextFieldState("")
        val telState = TextFieldState("")
        val addressState = TextFieldState("")
        val descriptionState = TextFieldState("")
        val accountOwnerState = TextFieldState("")
        val accountNumberState = TextFieldState("")

        private fun checkIncompleteFields(page: Int): List<Int> =
            when (page) {
                1 -> checkBasicInfoPageFields()
                2 -> checkLocationInfoPageFields()
                3 -> checkAccountInfoPageFields()
                else -> emptyList()
            }

        private fun checkBasicInfoPageFields(): List<Int> {
            val result = mutableListOf<Int>()
            if (sellerNameState.text.isEmpty()) result.add(0)
            if (businessRegistrationNumberState.text.length != 10) result.add(1)
            if (telState.text.length < 10) result.add(2)

            return result
        }

        private fun checkLocationInfoPageFields(): List<Int> {
            val result = mutableListOf<Int>()
            if (addressState.text.isEmpty()) result.add(0)

            return result
        }

        private fun checkAccountInfoPageFields(): List<Int> {
            val result = mutableListOf<Int>()
            if (accountOwnerState.text.isEmpty()) result.add(0)
            if (accountNumberState.text.isEmpty()) result.add(1)

            return result
        }

        fun clickNextButton() {
            viewModelScope.launch {
                val incomplete = checkIncompleteFields(_uiState.value.registrationStep)

                if (!incomplete.isEmpty()) {
                    _uiState.value =
                        SellerRegisterUiState.Error(
                            registrationStep = _uiState.value.registrationStep,
                            errorField = incomplete,
                        )
                    return@launch
                }

                if (_uiState.value.registrationStep == 3) {
                    _uiState.value = SellerRegisterUiState.Loading(_uiState.value.registrationStep)

                    repository.registerSellerInformation(
                        SellerInfo(
                            companyName = sellerNameState.text.toString(),
                            businessNumber = businessRegistrationNumberState.text.toString(),
                            phoneNumber = telState.text.toString(),
                            address = addressState.text.toString(),
                            latitude = 0.0,
                            longitude = 0.0,
                            postCode = "12345",
                            description = descriptionState.text.toString(),
                            detail = "",
                            detailAddress = "",
                            ownerName = accountOwnerState.text.toString(),
                            accountNumber = accountNumberState.text.toString(),
                        ),
                    )
                } else {
                    _uiState.value =
                        SellerRegisterUiState.Shown(
                            registrationStep = min(_uiState.value.registrationStep + 1, 3),
                        )
                }
            }
        }

        fun clickPrevButton() {
            viewModelScope.launch {
                _uiState.value =
                    SellerRegisterUiState.Shown(
                        registrationStep = max(_uiState.value.registrationStep - 1, 1),
                    )
            }
        }
    }
