package com.ssavice.seller_register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<SellerRegisterUiState>(
        SellerRegisterUiState.Shown(1)
    )
    val uiState:StateFlow<SellerRegisterUiState> = _uiState

    val sellerNameState = TextFieldState("")
    val businessRegistrationNumberState = TextFieldState("")
    val telState = TextFieldState("")
    val addressState = TextFieldState("")
    val descriptionState = TextFieldState("")
    val accountOwnerState = TextFieldState("")
    val accountNumberState = TextFieldState("")

    fun clickNextButton() {
        viewModelScope.launch {
            _uiState.value = (_uiState.value as? SellerRegisterUiState.Shown)?.copy(
                registrationStep = min(_uiState.value.registrationStep + 1, 3)
            )!!
        }
    }

    fun clickPrevButton() {
        viewModelScope.launch {
            _uiState.value = (_uiState.value as? SellerRegisterUiState.Shown)?.copy(
                registrationStep = max(_uiState.value.registrationStep - 1, 1)
            )!!
        }
    }
}
