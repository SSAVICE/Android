package com.ssavice.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.SsaviceRoundRectShape
import com.ssavice.designsystem.theme.SsaviceTheme
import java.time.Instant
import java.time.ZoneId

/**
 * A composable that looks like an Exposed Dropdown Menu but triggers a DatePickerDialog on click.
 * It displays the selected date in a formatted string and returns the selection as a timestamp.
 *
 * @param selectedTimestamp The currently selected date as a UTC+9 (KST) timestamp, or null if none.
 * @param onDateSelected Callback function invoked with the selected timestamp when a date is chosen.
 * @param modifier The modifier to be applied to the component.
 * @param labelText The label to display above the text field.
 */
@Composable
fun SsaviceDateSpinner(
    selectedTimestamp: Long?,
    onDateSelected: (timestamp: Long) -> Unit,
    modifier: Modifier = Modifier,
    text: String = "연도-월-일",
    labelText: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // Format the timestamp into a readable date string for display
    val formattedDate = remember(selectedTimestamp) {
        if (selectedTimestamp != null && selectedTimestamp != 0L) {
            val instant = Instant.ofEpochMilli(selectedTimestamp)
            val zonedDateTime = instant.atZone(ZoneId.of("Asia/Seoul"))
            // Format to "YYYY-MM-DD"
            "${zonedDateTime.year}-${
                zonedDateTime.monthValue.toString().padStart(2, '0')
            }-${zonedDateTime.dayOfMonth.toString().padStart(2, '0')}"
        } else {
            "" // Display nothing if no date is selected
        }
    }

    LabeledComponent(
        modifier,
        labelText,
        isError,
        errorMessage
    ) {
        Box {
            OutlinedTextField(
                value = formattedDate,
                onValueChange = {}, // Read-only, so this does nothing
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Open Date Picker"
                    )
                },
                shape = SsaviceRoundRectShape
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        onClick = { showDatePicker = true },
                        indication = null, // No ripple effect
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        }
    }

    // Show the dialog when the state is true
    if (showDatePicker) {
        SsaviceDatePickerDialog(
            onDateSelected = { timestamp ->
                onDateSelected(timestamp)
            },
            onDismiss = {
                showDatePicker = false
            },
            selectedTimestamp = if (selectedTimestamp?.equals(0L) == true) null else selectedTimestamp
        )
    }
}

/**
 * A Composable function that displays a Material 3 Date Picker Dialog.
 * The dialog allows users to select a date and returns the selection as a UTC timestamp
 * in milliseconds, adjusted for the UTC+9 (KST) timezone.
 *
 * @param onDateSelected Callback function that is invoked with the selected timestamp (in milliseconds)
 *                       when the user clicks the "Apply" button.
 * @param onDismiss Callback function that is invoked when the dialog is dismissed, either by
 *                  clicking the "Cancel" button or by clicking outside the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SsaviceDatePickerDialog(
    onDateSelected: (timestamp: Long) -> Unit,
    onDismiss: () -> Unit,
    selectedTimestamp: Long? = null
) {
    // KST (Korea Standard Time) is UTC+9
    val kstZoneId = ZoneId.of("Asia/Seoul")

    // Use rememberDatePickerState to manage the state of the DatePicker
    val datePickerState = rememberDatePickerState(
        // You can set an initial selected date, e.g., today in KST
        initialSelectedDateMillis = selectedTimestamp ?: Instant.now().atZone(kstZoneId).toInstant()
            .toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = {
                    // When "Apply" is clicked, get the selected date in UTC and then convert to KST
                    datePickerState.selectedDateMillis?.let { utcMillis ->
                        // The selectedDateMillis is in UTC. We interpret it as a local date
                        // in KST and get the corresponding timestamp.
                        val instant = Instant.ofEpochMilli(utcMillis)
                        val zonedDateTime = instant.atZone(kstZoneId)
                        onDateSelected(zonedDateTime.toInstant().toEpochMilli())
                    }
                    onDismiss()
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    ) {
        // The DatePicker itself
        DatePicker(state = datePickerState)
    }
}

@Preview
@Composable
private fun SsaviceDatePickerDialogPreview() {
    SsaviceTheme {
        // Example of how to use the dialog.
        // In a real app, you would control this with a mutable state.
        var showDialog by remember { mutableStateOf(true) }
        var selectedDate by remember { mutableStateOf<Long?>(null) }

        if (showDialog) {
            SsaviceDatePickerDialog(
                onDateSelected = { timestamp ->
                    selectedDate = timestamp
                    // In a real app, you might log this or update a ViewModel
                    println("Selected KST timestamp: $timestamp")
                },
                onDismiss = {
                    showDialog = false
                    println("Dialog dismissed")
                }
            )
        }
    }
}


// This replaces the existing preview content to show the new component
@Preview
@Composable
private fun SsaviceDateSpinnerPreview() {
    SsaviceTheme {
        SsaviceBackground {
            // State for holding the selected date timestamp
            var selectedTimestamp by remember { mutableStateOf<Long?>(null) }

            SsaviceDateSpinner(
                selectedTimestamp = selectedTimestamp,
                onDateSelected = { timestamp ->
                    selectedTimestamp = timestamp
                },
                modifier = Modifier
                    .padding(16.dp)
                    .padding(vertical = 100.dp)
            )
        }
    }
}
