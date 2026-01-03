package com.ssavice.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.theme.SsaviceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SsaviceTopBar(
    title: String,
    contentPadding: PaddingValues = TopAppBarDefaults.ContentPadding,
) {
    Column {
        TopAppBar(
            title = {
                ProvideTextStyle(value = MaterialTheme.typography.headlineMedium) {
                    Text(text = title, fontWeight = FontWeight.SemiBold)
                }
            },
            contentPadding = contentPadding,
        )
        HorizontalDivider(
            modifier = Modifier.padding(5.dp),
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SsavicePopUpTopBar(
    title: String,
    contentPadding: PaddingValues = TopAppBarDefaults.ContentPadding,
    onBackClicked: () -> Unit = {},
) {
    Column {
        TopAppBar(
            title = {
                ProvideTextStyle(value = MaterialTheme.typography.headlineSmall) {
                    Text(text = title, fontWeight = FontWeight.SemiBold)
                }
            },
            navigationIcon = {
                IconButton (onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            contentPadding = contentPadding,
        )
    }
}

@Preview
@Composable
private fun SsaviceTopBarPreview() {
    SsaviceTheme {
        SsaviceTopBar("SSAVICE")
    }
}

@Preview
@Composable
private fun SsavicePopUpTopBarPreview() {
    SsaviceTheme {
        SsavicePopUpTopBar("SSAVICE")
    }
}
