package com.ssavice.user_main

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.component.SsaviceBackground
import com.ssavice.designsystem.component.SsaviceChip
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.service.SortingOrder
import com.ssavice.ui.searchresult.SearchQuery
import com.ssavice.ui.searchresult.SearchResultScreen

@Composable
fun UserMainScreen(
    modifier: Modifier = Modifier,
    viewModel: UserMainViewModel = hiltViewModel(),
    onSearchBarClicked: () -> Unit = {}
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    UserMainScreen(
        modifier = modifier,
        state = state.value,
        onCategoryClicked = viewModel::onCategorySelect,
    )
}

@Composable
fun UserMainScreen (
    modifier: Modifier = Modifier,
    state: UserMainUiState,
    onSearchBarClicked: () -> Unit = {},
    onCategoryClicked: (Int) -> Unit = {}
) {
    SearchResultScreen(
        modifier = modifier,
        query = state.defaultSearchQuery
    ) {
        item {
            CategoryPicker(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp),
                categories = state.categories,
                selection = state.selected,
                onSelectionChanged = onCategoryClicked
            )
        }
    }
}

@Composable
fun CategoryPicker(
    modifier: Modifier,
    categories: List<String>,
    selection: Int,
    spacing: Dp = 10.dp,
    onSelectionChanged: (Int) -> Unit = {}
) {
    Row(modifier = modifier
        .horizontalScroll(rememberScrollState()),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing)) {
        categories.forEachIndexed { i, category ->
            SsaviceChip(
                selected = i == selection,
                onSelectedChange = { onSelectionChanged(i) },
                text = category
            )
        }
    }
}

@Preview
@Composable
fun CategoryPickerPreview() {
    val categories by remember { mutableStateOf(List(5) { "Category $it" }) }
    var selection by remember { mutableIntStateOf(0) }

    SsaviceTheme {
        SsaviceBackground(
            modifier = Modifier.size(540.dp,833.dp)
        ) {
            CategoryPicker(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp),
                categories = categories,
                selection = selection,
                onSelectionChanged = { selection = it }
            )
        }
    }

}

@Preview
@Composable
fun UserMainScreenPreview() {
    SsaviceTheme {
        SsaviceBackground(
            modifier = Modifier.size(540.dp,833.dp)
        ) {
            UserMainScreen()
        }
    }
}
