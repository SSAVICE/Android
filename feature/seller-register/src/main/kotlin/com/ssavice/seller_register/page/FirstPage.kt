package com.ssavice.seller_register.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.Button
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
import com.ssavice.designsystem.component.SsaviceButton
import com.ssavice.designsystem.component.SsaviceDateSpinner
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.model.TimeStamp
import com.ssavice.seller_register.RegisterScreenDefaults
import com.ssavice.seller_register.ValidationState

@Composable
fun FirstPage(
    modifier: Modifier = Modifier,
    businessRegistrationNumberState: TextFieldState,
    businessOwnerState: TextFieldState,
    businessNameState: TextFieldState,
    companyOpenDate: TimeStamp,
    companyValidationState: ValidationState,
    businessRegistrationNumberError: Boolean,
    businessOwnerError: Boolean,
    businessNameError: Boolean,
    tokenRemainingTime: Long,
    onCompanyOpenDateChanged: (TimeStamp) -> Unit,
    onValidateButtonClicked: () -> Unit,
) {
    val needValidation = companyValidationState == ValidationState.NotValidated
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
            Text(
                RegisterScreenDefaults.LABEL_FIRST_PAGE,
                fontWeight = FontWeight.Bold,
            )
        }

        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = businessOwnerState,
            placeholderText = RegisterScreenDefaults.BUSINESS_OWNER_PLACEHOLDER,
            labelText = RegisterScreenDefaults.BUSINESS_OWNER_TEXT,
            enabled = needValidation,
            isError = businessOwnerError,
            errorMessage = if (businessOwnerError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = businessNameState,
            placeholderText = RegisterScreenDefaults.BUSINESS_NAME_PLACEHOLDER,
            labelText = RegisterScreenDefaults.BUSINESS_NAME_TEXT,
            enabled = needValidation,
            isError = businessNameError,
            errorMessage = if (businessNameError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = businessRegistrationNumberState,
            placeholderText = RegisterScreenDefaults.SELLER_BUSINESS_REGISTRATION_NUMBER_PLACEHOLDER,
            labelText = RegisterScreenDefaults.SELLER_BUSINESS_REGISTRATION_NUMBER_TEXT,
            enabled = needValidation,
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
        SsaviceDateSpinner(
            modifier = Modifier.fillMaxWidth(),
            selectedTimestamp = if (companyOpenDate.timeInMillis == 0L) null else companyOpenDate.timeInMillis,
            onDateSelected = { onCompanyOpenDateChanged(TimeStamp(it)) },
            labelText = RegisterScreenDefaults.OPEN_DATE_TEXT,
            enabled = needValidation,
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            SsaviceButton(
                onClick = onValidateButtonClicked,
                text =
                    if (companyValidationState == ValidationState.Validated) {
                        RegisterScreenDefaults.VALIDATE_BUTTON_COMPLETE + " (${tokenRemainingTime}s)"
                    } else {
                        RegisterScreenDefaults.VALIDATE_BUTTON
                    },
                modifier = Modifier.width(150.dp).padding(bottom = 15.dp),
                enabled = needValidation,
            )
        }
    }
}
