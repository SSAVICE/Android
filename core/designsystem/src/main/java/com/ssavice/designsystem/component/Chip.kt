package com.ssavice.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.SsaviceLightGray
import com.ssavice.designsystem.theme.SsaviceTheme

@Composable
fun SsaviceChip(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
) {
    FilterChip(
        selected = selected,
        onClick = { onSelectedChange(!selected) },
        modifier =
            modifier
                .padding(0.dp)
                .wrapContentSize(),
        label = {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                Text(
                    text,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        },
        enabled = enabled,
        shape = RoundedCornerShape(24),
        border =
            FilterChipDefaults.filterChipBorder(
                enabled = enabled,
                selected = selected,
                borderColor = SsaviceLightGray,
                selectedBorderColor = Color.Transparent,
                borderWidth = SsaviceChipDefaults.ChipBorderWidth,
            ),
        colors =
            FilterChipDefaults.filterChipColors(
                labelColor = MaterialTheme.colorScheme.onBackground,
                iconColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor =
                    if (selected) {
                        MaterialTheme.colorScheme.onBackground.copy(
                            alpha = SsaviceChipDefaults.DISABLED_CHIP_CONTAINER_ALPHA,
                        )
                    } else {
                        Color.Transparent
                    },
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            ),
    )
}

@Preview
@Composable
private fun ChipPreview() {
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(60.dp, 20.dp)) {
            SsaviceChip(selected = true, onSelectedChange = {}, text = "전체")
        }
    }
}

@Preview
@Composable
private fun ChipPreviewList() {
    val items =
        remember {
            mutableStateOf(
                List(5) { Pair(it, "Item $it") },
            )
        }
    var selection by remember { mutableIntStateOf(0) }

    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(400.dp, 50.dp)) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                items(items.value, key = { it.first }) {
                    SsaviceChip(
                        selected = selection == it.first,
                        onSelectedChange = { t ->
                            selection = it.first
                        },
                        text = it.second,
                    )
                }
            }
        }
    }
}

object SsaviceChipDefaults {
    const val DISABLED_CHIP_CONTAINER_ALPHA = 0.12f
    val ChipBorderWidth = 0.4.dp
}
