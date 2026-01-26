package com.ssavice.user_main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.LabeledComponent
import com.ssavice.designsystem.component.OutlinedTextFieldButton
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.mappicker.AddressPickerDialog
import com.ssavice.user_main.AddressBottomSheetConstants.ADDRESS_TEXT
import com.ssavice.user_main.AddressBottomSheetConstants.APPLY_TEXT
import com.ssavice.user_main.AddressBottomSheetConstants.DETAIL_ADDRESS_PLACEHOLDER
import com.ssavice.user_main.AddressBottomSheetConstants.DETAIL_ADDRESS_TEXT

@Composable
fun AddressSelectForm(
    modifier: Modifier = Modifier,
    onConfirmChange: (RegionState.Showing) -> Unit,
    initialState: RegionState.Showing
) {
    var showAddressPicker by remember { mutableStateOf(false) }
    val detailAddressState = rememberTextFieldState(initialState.detailAddress)
    var modifiedAddress by remember { mutableStateOf(initialState) }
    val savable = detailAddressState.text.toString().isNotBlank()

    Column(
        modifier = modifier.padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        LabeledComponent(
            Modifier,
            ADDRESS_TEXT
        ) {
            OutlinedTextFieldButton(
                placeHolder = "",
                text = modifiedAddress.address,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Open Date Picker",
                    )
                },
                onClick = { if (!showAddressPicker) showAddressPicker = true },
            )
        }

        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = detailAddressState,
            placeholderText = DETAIL_ADDRESS_PLACEHOLDER,
            isError = false,
            labelText = DETAIL_ADDRESS_TEXT,
        )

        Spacer(Modifier.height(10.dp))
        SsaviceButton(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 5.dp),
            text = APPLY_TEXT,
            enabled = savable,
            onClick = {
                onConfirmChange(modifiedAddress.copy(
                    detailAddress = detailAddressState.text.toString()
                ))
            }
        )
    }

    if (showAddressPicker) {
        AddressPickerDialog(
            {
                modifiedAddress = RegionState.Showing(
                    address = it.address,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    postCode = it.zipCode,
                    regionCode = it.regionCode,
                    detailAddress = ""
                )
                showAddressPicker = false
            },
            onDismiss = {
                showAddressPicker = false
            },
        )
    }
}

@Preview
@Composable
fun PreviewAddressSelectForm() {
    val state = RegionState.Showing(
        address = "서울특별시 강남구 역삼동 123-45",
        latitude = 37.5123456,
        longitude = 127.0,
        postCode = "1234",
        regionCode = "12345",
        detailAddress = "101동 1001호"
    )

    SsaviceTheme {

        Scaffold { innerPadding ->
            AddressSelectForm(
                modifier = Modifier.padding(innerPadding),
                onConfirmChange = {},
                initialState = state
            )
        }
    }
}

object AddressBottomSheetConstants {
    const val ADDRESS_TEXT = "주소"
    const val DETAIL_ADDRESS_TEXT = "상세 주소"
    const val DETAIL_ADDRESS_PLACEHOLDER = "101동 1001호"
    const val APPLY_TEXT = "저장"
}
