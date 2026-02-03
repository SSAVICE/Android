package com.ssavice.mappicker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.ssavice.designsystem.component.OutlinedTextFieldButton

@Composable
fun AddressPickerSelectButton(
    addressText: String,
    placeHolder: String = "",
    onClick: () -> Unit
) {
    OutlinedTextFieldButton(
        placeHolder = placeHolder,
        text = addressText,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Open Date Picker",
            )
        },
        onClick = onClick,
    )
}
