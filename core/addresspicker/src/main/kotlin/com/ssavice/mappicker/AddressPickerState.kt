package com.ssavice.mappicker

import com.ssavice.mappicker.model.AddressPickResult

sealed interface AddressPickerState {
    object Idle : AddressPickerState

    object Loading : AddressPickerState

    data class Done(
        val result: AddressPickResult,
    ) : AddressPickerState
}
