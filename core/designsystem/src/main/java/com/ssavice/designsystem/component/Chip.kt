package com.ssavice.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    innerPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
) {
    // FilterChip의 색상 로직을 그대로 가져옵니다.
    val containerColor =
        when {
            !enabled && selected -> MaterialTheme.colorScheme.onBackground.copy(alpha = SsaviceChipDefaults.DISABLED_CHIP_CONTAINER_ALPHA)
            selected -> MaterialTheme.colorScheme.primary
            else -> Color.Transparent
        }
    val labelColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground

    // FilterChip의 테두리 로직을 그대로 가져옵니다.
    val border =
        when {
            !enabled -> BorderStroke(SsaviceChipDefaults.ChipBorderWidth, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

            selected -> null

            // 선택 시 테두리 없음
            else -> BorderStroke(SsaviceChipDefaults.ChipBorderWidth, SsaviceLightGray)
        }

    // 1. 핵심: FilterChip 대신 Surface 사용
    Surface(
        modifier =
            modifier
                .clickable(
                    enabled = enabled,
                    onClick = { onSelectedChange(selected) },
                ),
        shape = RoundedCornerShape(6.dp),
        color = containerColor,
        border = border,
    ) {
        Box(
            modifier =
                Modifier
                    .defaultMinSize(minWidth = 32.dp)
                    .padding(innerPadding),
            // <-- 원하는 패딩 값으로 조절!
            contentAlignment = Alignment.Center,
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
                Text(
                    text = text,
                    color = labelColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }
    }
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
                List(5) { Pair(it, "Item $it") }.plus(
                    List(2) { Pair(it + 5, "짧$it") },
                ),
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
