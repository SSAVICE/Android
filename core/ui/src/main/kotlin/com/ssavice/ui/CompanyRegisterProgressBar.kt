package com.ssavice.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.SsaviceBackground
import com.ssavice.designsystem.theme.SsaviceTheme

@Composable
fun WideBar(
    active: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
       modifier = modifier.height(6.dp)
           .background(if(active) MaterialTheme.colorScheme.primary else Color.LightGray, RoundedCornerShape(12.dp))
    )
}

@Composable
fun ProgressBar(
    maxStep: Int,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for(i in 0 until maxStep) {
            WideBar(
                active = i < currentStep,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview
@Composable
fun WideBarPreview() {
    SsaviceTheme {
        SsaviceBackground(Modifier.size(width = 400.dp, height = 300.dp)) {
            Box(modifier = Modifier.fillMaxSize()) {
                ProgressBar(
                    3, 0, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
