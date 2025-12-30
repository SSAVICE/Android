package com.ssavice.seller_register

sealed interface SellerRegisterUiState {
    val registrationStep: Int
    val submitted: Boolean

    data class Loading(
        override val registrationStep: Int = 1,
        override val submitted: Boolean = false,
    ) : SellerRegisterUiState

    data class Shown(
        override val registrationStep: Int = 1,
        override val submitted: Boolean = false,
    ) : SellerRegisterUiState

    data class Error(
        override val registrationStep: Int = 1,
        override val submitted: Boolean = false,
        val errorField: List<Int>,
    ) : SellerRegisterUiState
}
