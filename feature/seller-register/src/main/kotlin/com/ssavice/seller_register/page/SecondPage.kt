package com.ssavice.seller_register.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.seller_register.RegisterScreenDefaults

@Composable
fun SecondPage(
    modifier: Modifier = Modifier,
    addressState: TextFieldState,
    descriptionState: TextFieldState,
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
            state = addressState,
            placeholderText = RegisterScreenDefaults.ADDRESS_PLACEHOLDER,
            labelText = RegisterScreenDefaults.ADDRESS_TEXT,
            isError = addressError,
            errorMessage = if (addressError) RegisterScreenDefaults.FIELD_ERROR_MESSAGE else null,
        )
        SsaviceInputField(
            modifier = Modifier.fillMaxWidth(),
            state = descriptionState,
            multiLine = true,
            placeholderText = RegisterScreenDefaults.DESCRIPTION_PLACEHOLDER,
            labelText = RegisterScreenDefaults.DESCRIPTION_TEXT,
        )
    }
}
