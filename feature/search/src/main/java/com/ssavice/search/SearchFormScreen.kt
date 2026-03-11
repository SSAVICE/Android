package com.ssavice.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssavice.designsystem.component.AdjustedSlider
import com.ssavice.designsystem.component.SsaviceBackground
import com.ssavice.designsystem.component.SsaviceChip
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SearchRange
import com.ssavice.model.enums.SortingOrder
import com.ssavice.ui.common.Constant
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

@Composable
fun SearchFormScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchFormViewModel = hiltViewModel(),
    onSearch: (SearchForm) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var shouldRenderContent by remember {
        mutableStateOf(false)
    }

    val query = rememberTextFieldState(state.form.query)
    LaunchedEffect(query) {
        snapshotFlow { query.text.toString() }.collect {
            viewModel.onQuery(it)
        }
    }

    // 포커스 요청을 위한 FocusRequester 생성
    val focusRequester = remember { FocusRequester() }

    // 화면이 처음 그려질 때 포커스를 요청
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        viewModel.initiateRegion()
    }

    LaunchedEffect(shouldRenderContent) {
        if (!shouldRenderContent) {
            delay(Constant.ANIMATION_DELAY)
            shouldRenderContent = true
        }
    }

    SearchFormScreen(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.background),
        form = state.form,
        query = query,
        onCategoryChange = viewModel::onCategorySelect,
        onSearchRangeChange = viewModel::onSearchRangeSelect,
        onPriceRangeChange = viewModel::onPriceRangeChange,
        onSortByChange = viewModel::onSortByChange,
        onSearchClick = {
            onSearch(state.form)
        },
        onSaleOnlyChange = viewModel::onSaleOnlyChanged,
        focusRequester = focusRequester,
        readyToRenderContent = shouldRenderContent,
        region1 = state.region1String,
        region2 = state.region2String,
    )
}

@Composable
fun SearchFormScreen(
    modifier: Modifier = Modifier,
    form: SearchForm,
    query: TextFieldState,
    onCategoryChange: (Category) -> Unit = {},
    onSearchRangeChange: (SearchRange) -> Unit = {},
    onPriceRangeChange: (IntRange) -> Unit = {},
    onSortByChange: (SortingOrder) -> Unit = {},
    onSearchClick: (query: String) -> Unit = {},
    onSaleOnlyChange: (Boolean) -> Unit = {},
    focusRequester: FocusRequester? = null,
    readyToRenderContent: Boolean = true,
    region1: String = "",
    region2: String = "",
) {
    Column(modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SsaviceInputField(
                state = query,
                modifier =
                    if (focusRequester != null) {
                        Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                    } else {
                        Modifier
                            .weight(1f)
                    },
                placeholderText = "서비스, 태그 검색 ...",
                onSubmit = {
                    onSearchClick(query.text.toString())
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            )
            Spacer(modifier = Modifier.width(2.dp))
            IconButton(
                onClick = { onSearchClick(query.text.toString()) },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                )
            }
        }
        HorizontalDivider(
            modifier =
                Modifier
                    .padding(horizontal = 5.dp)
                    .padding(top = 10.dp),
        )

        if (!readyToRenderContent) return@Column
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 15.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 15.dp),
                ) {
                    InnerFieldWithLabel("카테고리") {
                        FlowRow(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
                            horizontalArrangement = spacedBy(5.dp, alignment = Alignment.Start),
                            verticalArrangement = spacedBy(10.dp),
                        ) {
                            form.categories.filter { it.showInUser }.forEach { category ->
                                SsaviceChip(
                                    text = category.value,
                                    selected = form.selectedCategory == category,
                                    onSelectedChange = { onCategoryChange(category) },
                                )
                            }
                        }
                    }

                    InnerFieldWithLabel("검색 범위") {
                        FlowRow(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
                            horizontalArrangement = spacedBy(5.dp, alignment = Alignment.Start),
                            verticalArrangement = spacedBy(10.dp),
                        ) {
                            listOf(region1, region2, "1.5km", "3km").forEachIndexed { index, range ->
                                SearchRange.entries.getOrNull(index)?.run {
                                    SsaviceChip(
                                        text = range,
                                        selected = this == form.searchRange,
                                        onSelectedChange = { onSearchRangeChange(this) },
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.padding(vertical = 5.dp))

                        val checkboxInteractionSource = remember { MutableInteractionSource() }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.fillMaxWidth().clickable(
                                    interactionSource = checkboxInteractionSource,
                                    indication = null,
                                    onClick = { onSaleOnlyChange(!form.onSaleOnly) },
                                ),
                        ) {
                            Text(
                                text = "참여 가능한 서비스만 조회",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Checkbox(
                                checked = form.onSaleOnly,
                                onCheckedChange = { onSaleOnlyChange(it) },
                                modifier = Modifier.padding(0.dp),
                                interactionSource = checkboxInteractionSource,
                            )
                        }
                    }

                    InnerFieldWithLabel("가격") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(200.dp),
                        ) {
                            Text(text = "최소", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedTextField(
                                value = formatPrice(form.priceRange.first),
                                maxLines = 1,
                                modifier = Modifier.weight(1f),
                                onValueChange = {
                                    val new =
                                        min(
                                            min(
                                                max(it.filter { it.isDigit() }.toIntOrNull() ?: 0, 0),
                                                10_000_000,
                                            ),
                                            form.priceRange.last,
                                        )
                                    onPriceRangeChange(new..form.priceRange.last)
                                },
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "원", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier
                                    .width(200.dp),
                        ) {
                            Text(text = "최대", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedTextField(
                                value = formatPrice(form.priceRange.last),
                                maxLines = 1,
                                modifier = Modifier.weight(1f),
                                onValueChange = {
                                    val new =
                                        max(
                                            min(
                                                max(it.filter { it.isDigit() }.toIntOrNull() ?: 0, 0),
                                                10_000_000,
                                            ),
                                            form.priceRange.first,
                                        )
                                    onPriceRangeChange(form.priceRange.first..new)
                                },
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "원", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        AdjustedSlider(
                            onValueChange = {
                                onPriceRangeChange(it)
                            },
                            values = (form.priceRange),
                        )
                    }

                    InnerFieldWithLabel("정렬") {
                        Column(
                            verticalArrangement = spacedBy(5.dp),
                        ) {
                            SortingOrder.entries.forEach { s ->
                                SsaviceChip(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp),
                                    text = s.value,
                                    selected = form.sortBy == s,
                                    onSelectedChange = { onSortByChange(s) },
                                    innerPadding = PaddingValues(vertical = 10.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatPrice(price: Int): String = String.format("%,d", price)

@Composable
private fun InnerFieldWithLabel(
    label: String,
    content: @Composable () -> Unit,
) {
    Text(
        modifier = Modifier.padding(bottom = 12.dp),
        text = label,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
    )
    content()
    Spacer(modifier = Modifier.padding(vertical = 20.dp))
}

@Preview(showSystemUi = false)
@Composable
fun SearchFormPreview() {
    val query = rememberTextFieldState("")
    var priceRange by remember { mutableStateOf(500..100000) }
    SsaviceTheme {
        SsaviceBackground(modifier = Modifier.size(540.dp, 720.dp)) {
            SearchFormScreen(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(),
                form =
                    SearchForm(
                        query = query.text.toString(),
                        categories = Category.entries,
                        selectedCategory = Category.entries.filter { it.showInUser }[0],
                        searchRange = SearchRange.entries[0],
                        priceRange = priceRange,
                        sortBy = SortingOrder.entries[0],
                        onSaleOnly = false,
                    ),
                query = query,
                onSearchClick = {
                    query.clearText()
                },
                onPriceRangeChange = {
                    priceRange = it
                },
            )
        }
    }
}
