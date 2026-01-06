package com.ssavice.search_result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.component.OutlinedTextFieldButton
import com.ssavice.model.service.SearchQuery
import com.ssavice.ui.searchresult.SearchResultScreen

@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchResultViewModel = hiltViewModel(),
    onSearchBarClicked: (searchQuery: SearchQuery) -> Unit = {},
    onBackClicked: () -> Unit = {},
    onServiceClicked: (Long) -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    SearchResultScreen(
        modifier = modifier,
        state = state.value,
        onSearchBarClicked = onSearchBarClicked,
        onServiceClicked = onServiceClicked,
    )
}

@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    state: SearchResultUiState,
    onSearchBarClicked: (searchQuery: SearchQuery) -> Unit = {},
    onServiceClicked: (Long) -> Unit = {},
) {
    Column(modifier = modifier) {
        OutlinedTextFieldButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
            placeHolder = "서비스, 태그 검색 ...",
            text = state.searchQuery.query,
            onClick = { onSearchBarClicked(state.searchQuery) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                )
            },
        )
        SearchResultScreen(
            modifier = Modifier.weight(1f),
            query = state.searchQuery,
            onServiceClick = onServiceClicked,
        )
    }
}
