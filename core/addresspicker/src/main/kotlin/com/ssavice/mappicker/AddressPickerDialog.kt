package com.ssavice.mappicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
    ) {
        Card(
            modifier = Modifier.height(500.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = Color.White
            )
        ) {
            AddressPickerWebView(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                onResult = onSelect,
            )
        }
    }
}
