package com.ssavice.seller_register.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.InputTransformations.digitOnlyInputTransformation
import com.ssavice.designsystem.component.LabeledComponent
import com.ssavice.designsystem.component.OutlinedTextFieldButton
import com.ssavice.designsystem.component.OutputTransformations
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.mappicker.AddressPickerDialog
import com.ssavice.seller_register.AddressForm
import com.ssavice.seller_register.RegisterScreenDefaults

@Composable
fun SecondPage(
    modifier: Modifier = Modifier,
    detailAddressState: TextFieldState,
    addressState: AddressForm,
    sellerNameError: Boolean,
    descriptionState: TextFieldState,
    sellerNameState: TextFieldState,
    telState: TextFieldState,
    onAddressSelected: (AddressForm) -> Unit,
    telError: Boolean,
    addressError: Boolean,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Text(
                RegisterScreenDefaults.LABEL_SECOND_PAGE,
                fontWeight = FontWeight.Bold,
            )
        }
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = sellerNameState,
            placeholderText = RegisterScreenDefaults.SELLER_NAME_PLACEHOLDER,
            labelText = RegisterScreenDefaults.SELLER_NAME_TEXT,
            isError = sellerNameError,
            errorMessage = if (sellerNameError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = telState,
            placeholderText = RegisterScreenDefaults.SELLER_TEL_PLACEHOLDER,
            labelText = RegisterScreenDefaults.SELLER_TEL_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation =
                InputTransformation.maxLength(11).then(
                    digitOnlyInputTransformation,
                ),
            outputTransformation = OutputTransformations.formatPhoneNumber,
            isError = telError,
            errorMessage = if (telError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        Spacer(Modifier.height(15.dp))
        InputAddressScreen(
            modifier = Modifier.fillMaxWidth(),
            isError = addressError,
            errorMessage = if (addressError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
            addressState = addressState,
            onAddressSelected = onAddressSelected,
            detailAddressState = detailAddressState,
        )

        Spacer(Modifier.height(15.dp))
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = descriptionState,
            multiLine = true,
            placeholderText = RegisterScreenDefaults.DESCRIPTION_PLACEHOLDER,
            labelText = RegisterScreenDefaults.DESCRIPTION_TEXT,
        )
    }
}

@Composable
private fun InputAddressScreen(
    modifier: Modifier,
    isError: Boolean,
    errorMessage: String?,
    addressState: AddressForm,
    onAddressSelected: (AddressForm) -> Unit,
    detailAddressState: TextFieldState,
) {
    var showAddressPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        LabeledComponent(
            Modifier,
            RegisterScreenDefaults.ADDRESS_TEXT,
            isError,
            errorMessage,
        ) {
            OutlinedTextFieldButton(
                placeHolder = RegisterScreenDefaults.ADDRESS_PLACEHOLDER,
                text = addressState.address,
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
            placeholderText = RegisterScreenDefaults.ADDRESS_DETAIL_PLACEHOLDER,
            isError = false,
            labelText = RegisterScreenDefaults.DETAIL_ADDRESS_TEXT,
        )
    }

    if (showAddressPicker) {
        AddressPickerDialog(
            {
                onAddressSelected(
                    AddressForm(
                        it.address,
                        it.regionCode,
                        it.latitude,
                        it.longitude,
                        it.zipCode,
                    ),
                )
                showAddressPicker = false
            },
            onDismiss = {
                showAddressPicker = false
            },
        )
    }
}
