package com.ssavice.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.SsaviceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SsaviceDropdown(
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    selectedOption: String,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val shape = RoundedCornerShape(8.dp)
    var expanded by remember { mutableStateOf(false) }
    LabeledComponent(
        modifier,
        labelText,
        isError,
        errorMessage
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                shape = shape,
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = "Open Dropdown")
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun SsaviceDropdownPreview() {
    val options = listOf("Option 1", "Option 2", "Option 3")

    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(350.dp, 600.dp)) {
            SsaviceDropdown(
                options = options,
                modifier = Modifier.size(300.dp).padding(horizontal = 10.dp, vertical = 200.dp),
                selectedOption = options[0],
                onOptionSelected = {},
                labelText = "카테고리"
            )
        }
    }
}
