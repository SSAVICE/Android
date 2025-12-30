package com.ssavice.seller_register.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.InputTransformations.digitOnlyInputTransformation
import com.ssavice.designsystem.component.OutputTransformations
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.seller_register.RegisterScreenDefaults

@Composable
fun FirstPage(
    modifier: Modifier = Modifier,
    sellerNameState: TextFieldState,
    businessRegistrationNumberState: TextFieldState,
    businessOwnerState: TextFieldState,
    telState: TextFieldState,
    sellerNameError: Boolean,
    businessRegistrationNumberError: Boolean,
    telError: Boolean,
    businessOwnerError: Boolean
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Text(
                RegisterScreenDefaults.LABEL_FIRST_PAGE,
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
            state = businessOwnerState,
            placeholderText = RegisterScreenDefaults.BUSINESS_OWNER_PLACEHOLDER,
            labelText = RegisterScreenDefaults.BUSINESS_OWNER_TEXT,
            isError = businessOwnerError,
            errorMessage = if (sellerNameError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = businessRegistrationNumberState,
            placeholderText = RegisterScreenDefaults.SELLER_BUSINESS_REGISTRATION_NUMBER_PLACEHOLDER,
            labelText = RegisterScreenDefaults.SELLER_BUSINESS_REGISTRATION_NUMBER_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation =
                InputTransformation.maxLength(10).then(
                    digitOnlyInputTransformation,
                ),
            outputTransformation = OutputTransformations.formatBusinessNumber,
            isError = businessRegistrationNumberError,
            errorMessage = if (businessRegistrationNumberError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
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
    }
}
