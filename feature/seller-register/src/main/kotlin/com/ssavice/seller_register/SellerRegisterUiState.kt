package com.ssavice.seller_register

sealed interface SellerRegisterUiState {
    val registrationStep: Int

    data class Loading(
        override val registrationStep: Int = 1
    ): SellerRegisterUiState

    data class Shown(
        override val registrationStep: Int = 1
    ): SellerRegisterUiState

    data class Error(
        override val registrationStep: Int = 1,
        val errorField: List<Int>
    ): SellerRegisterUiState
}
