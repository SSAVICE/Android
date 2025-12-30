package com.ssavice.seller_register.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.InputTransformations
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.seller_register.RegisterScreenDefaults

@Composable
fun ThirdPage(
    modifier: Modifier = Modifier,
    accountOwnerState: TextFieldState,
    accountNumberState: TextFieldState,
    accountOwnerError: Boolean,
    accountNumberError: Boolean,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Text(
                RegisterScreenDefaults.LABEL_THIRD_PAGE,
                fontWeight = FontWeight.Bold,
            )
        }

        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = accountOwnerState,
            placeholderText = RegisterScreenDefaults.ACCOUNT_NAME_PLACEHOLDER,
            labelText = RegisterScreenDefaults.ACCOUNT_NAME_TEXT,
            isError = accountOwnerError,
            errorMessage = if (accountOwnerError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = accountNumberState,
            placeholderText = RegisterScreenDefaults.ACCOUNT_NUMBER_PLACEHOLDER,
            labelText = RegisterScreenDefaults.ACCOUNT_NUMBER_TEXT,
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
            inputTransformation = InputTransformations.digitOnlyInputTransformation,
            isError = accountNumberError,
            errorMessage = if (accountNumberError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
    }
}
