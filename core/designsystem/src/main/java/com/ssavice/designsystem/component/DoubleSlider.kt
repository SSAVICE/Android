package com.ssavice.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoubleSlider(
    modifier: Modifier = Modifier,
    values: IntRange,
    valueRange: IntRange = 0..100,
    onValueChange: (IntRange) -> Unit
) {
    val floatRange = valueRange.first.toFloat()..valueRange.last.toFloat()
    val sliderPosition = values.first.toFloat()..values.last.toFloat()

    Column(modifier = modifier) {
        RangeSlider(
            value = sliderPosition,
            onValueChange = {
                onValueChange(it.start.roundToInt()..it.endInclusive.roundToInt())
            },
            valueRange = floatRange,
            startThumb = { SliderDefaults.Thumb(interactionSource = remember { MutableInteractionSource() }) },
            endThumb = { SliderDefaults.Thumb(interactionSource = remember { MutableInteractionSource() }) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdjustedSlider(
    modifier: Modifier = Modifier,
    values: IntRange,
    valueRange: IntRange = 0..10_000_000,
    onValueChange: (IntRange) -> Unit,
) = with(DoubleSlider) {

    val totalRawSteps = SEG1_RAW_STEPS + SEG2_RAW_STEPS + SEG3_RAW_STEPS + SEG4_RAW_STEPS // 74
    val adjustedRange = (0f..totalRawSteps.toFloat())

    fun displayToRaw(displayValue: Int): Float {
        return when {
            displayValue < 0 -> 0f
            displayValue <= SEG1_MAX -> {
                displayValue.toFloat() / SEG1_STEP
            }
            displayValue <= SEG2_MAX -> {
                SEG1_RAW_STEPS.toFloat() + (displayValue - SEG1_MAX).toFloat() / SEG2_STEP
            }
            displayValue <= SEG3_MAX -> {
                (SEG1_RAW_STEPS + SEG2_RAW_STEPS).toFloat() + (displayValue - SEG2_MAX).toFloat() / SEG3_STEP
            }
            displayValue <= SEG4_MAX -> {
                (SEG1_RAW_STEPS + SEG2_RAW_STEPS + SEG3_RAW_STEPS).toFloat() + (displayValue - SEG3_MAX).toFloat() / SEG4_STEP
            }
            else -> totalRawSteps.toFloat()
        }
    }

    fun rawToDisplay(rawValue: Float): Int {
        val rawStep = rawValue.roundToInt()
        return when {
            rawStep <= SEG1_RAW_STEPS -> {
                rawStep * SEG1_STEP
            }
            rawStep <= SEG1_RAW_STEPS + SEG2_RAW_STEPS -> {
                SEG1_MAX + ((rawStep - SEG1_RAW_STEPS) * SEG2_STEP)
            }
            rawStep <= SEG1_RAW_STEPS + SEG2_RAW_STEPS + SEG3_RAW_STEPS -> {
                SEG2_MAX + ((rawStep - (SEG1_RAW_STEPS + SEG2_RAW_STEPS)) * SEG3_STEP)
            }
            else -> {
                val calculated = SEG3_MAX + ((rawStep - (SEG1_RAW_STEPS + SEG2_RAW_STEPS + SEG3_RAW_STEPS)) * SEG4_STEP)
                minOf(calculated, SEG4_MAX)
            }
        }
    }

    val adjustedValues = (displayToRaw(values.first)..displayToRaw(values.last))

    Column(modifier = modifier) {
        RangeSlider(
            value = adjustedValues,
            onValueChange = { newRawRange ->
                val newDisplayRange = IntRange(
                    rawToDisplay(newRawRange.start),
                    rawToDisplay(newRawRange.endInclusive)
                )
                if (newDisplayRange != values) {
                    onValueChange(newDisplayRange)
                }
            },
            valueRange = adjustedRange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DoubleSliderPreview() {
    var values by remember { mutableStateOf(10..90) }
    DoubleSlider(
        valueRange = 0..100,
        values = values,
        onValueChange = { values = it }
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun AdjustedSliderPreview() {
    Column(modifier = Modifier.padding(15.dp)) {

        var sliderValue2 by remember { mutableStateOf(500..100000) }
        Text("Value: (${sliderValue2.first}, ${sliderValue2.last})")
        AdjustedSlider(
            values = sliderValue2,
            onValueChange = { sliderValue2 = it }
        )
    }
}

object DoubleSlider{
    const val SEG1_MAX = 10_000
    const val SEG1_STEP = 500
    const val SEG1_RAW_STEPS = SEG1_MAX / SEG1_STEP // 20
    const val SEG2_MAX = 100_000
    const val SEG2_STEP = 5_000
    const val SEG2_RAW_STEPS = (SEG2_MAX - SEG1_MAX) / SEG2_STEP // 18
    const val SEG3_MAX = 1_000_000
    const val SEG3_STEP = 50_000
    const val SEG3_RAW_STEPS = (SEG3_MAX - SEG2_MAX) / SEG3_STEP // 18
    const val SEG4_MAX = 10_000_000
    const val SEG4_STEP = 500_000
    const val SEG4_RAW_STEPS = (SEG4_MAX - SEG3_MAX) / SEG4_STEP // 18
}
