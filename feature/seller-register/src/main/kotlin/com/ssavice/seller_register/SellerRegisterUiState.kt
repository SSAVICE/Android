package com.ssavice.seller_register

internal data class SellerRegisterUiState(
    val form: Form,
    val submitState: SubmitState,
)

internal data class Form(
    val registrationStep: Int,
    val sellerName: String,
    val businessOwnerName: String,
    val businessRegistrationNumber: String,
    val tel: String,
    val address: String,
    val description: String,
    val accountDepositor: String,
    val accountNumber: String,

    val sellerNameErrorState: FormError = FormError.None,
    val businessOwnerNameErrorState: FormError = FormError.None,
    val businessRegistrationNumberErrorState: FormError = FormError.None,
    val telErrorState: FormError = FormError.None,
    val addressErrorState: FormError = FormError.None,
    val descriptionErrorState: FormError = FormError.None,
    val accountDepositorErrorState: FormError = FormError.None,
    val accountNumberErrorState: FormError = FormError.None
)

internal sealed interface SubmitState {
    object Loading : SubmitState

    object Shown : SubmitState

    data class Error(val message: String) : SubmitState

    object Submit : SubmitState
}

enum class FormError { None, EmptyField, InsufficientField, InvalidField }
