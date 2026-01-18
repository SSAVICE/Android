package com.ssavice.mappicker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ssavice.mappicker.model.AddressPickResult

@Composable
fun AddressPickerDialog(
    onSelect: (AddressPickResult) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
    ) {
        Card(
            modifier = Modifier.height(400.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            AddressPickerWebView(
                modifier = Modifier.fillMaxSize(),
                onResult = onSelect
            )
        }
    }
}
