package com.ssavice.seller_register

import com.ssavice.model.TimeStamp

data class SellerRegisterUiState(
    val form: Form,
    val submitState: SubmitState,
    val showTestButton: Boolean,
    val companyValidationState: ValidationState,
    val tokenRemainingTime: Long,
)

data class AddressForm(
    val address: String,
    val regionCode: String,
    val latitude: Double,
    val longitude: Double,
    val zipCode: String,
)

data class Form(
    val registrationStep: Int,
    val sellerName: String,
    val businessOwnerName: String,
    val companyOpenDate: TimeStamp,
    val businessRegistrationNumber: String,
    val tel: String,
    val detailAddress: String,
    val address: AddressForm,
    val description: String,
    val accountDepositor: String,
    val accountNumber: String,
    val sellerNameErrorState: FormError = FormError.None,
    val businessOwnerNameErrorState: FormError = FormError.None,
    val companyOpenDateErrorState: FormError = FormError.None,
    val businessRegistrationNumberErrorState: FormError = FormError.None,
    val telErrorState: FormError = FormError.None,
    val addressErrorState: FormError = FormError.None,
    val descriptionErrorState: FormError = FormError.None,
    val accountDepositorErrorState: FormError = FormError.None,
    val accountNumberErrorState: FormError = FormError.None,
)

sealed interface SubmitState {
    object Loading : SubmitState

    object Shown : SubmitState

    data class Error(
        val message: String,
    ) : SubmitState

    object Submit : SubmitState
}

sealed interface ValidationState {
    object Validated : ValidationState

    object NotValidated : ValidationState

    object Validating : ValidationState

    object Failed : ValidationState
}

enum class FormError { None, EmptyField, InsufficientField, InvalidField }
