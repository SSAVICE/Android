package com.ssavice.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssavice.designsystem.component.AdjustedSlider
import com.ssavice.designsystem.component.SsaviceBackground
import com.ssavice.designsystem.component.SsaviceChip
import com.ssavice.designsystem.component.SsaviceInputField
import com.ssavice.designsystem.theme.SsaviceTheme
import com.ssavice.model.service.SortingOrder
import kotlin.math.max
import kotlin.math.min

@Composable
fun SearchFormScreen(
    modifier: Modifier = Modifier,
    form: SearchForm,
    query: TextFieldState,
    onCategoryChange: (Int) -> Unit = {},
    onSearchRangeChange: (Int) -> Unit = {},
    onPriceRangeChange: (IntRange) -> Unit = {},
    onSortByChange: (SortingOrder) -> Unit = {},
    onSearchClick: (query: String) -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SsaviceInputField(
                state = query,
                modifier = Modifier.weight(1f),
                placeholderText = "서비스, 태그 검색 ...",
                onSubmit = {
                    onSearchClick(query.text.toString())
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )
            Spacer(modifier = Modifier.width(2.dp))
            IconButton(
                onClick = { onSearchClick(query.text.toString()) }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }
        HorizontalDivider(modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 10.dp))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 15.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 15.dp)
                ) {

                    InnerFieldWithLabel("카테고리") {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = spacedBy(5.dp, alignment = Alignment.Start),
                            verticalArrangement = spacedBy(10.dp)
                        ) {
                            form.categories.forEachIndexed { index, category ->
                                SsaviceChip(
                                    text = category,
                                    selected = form.selectedCategory == index,
                                    onSelectedChange = { onCategoryChange(index) },
                                )
                            }
                        }
                    }

                    InnerFieldWithLabel("검색 범위") {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = spacedBy(5.dp, alignment = Alignment.Start),
                            verticalArrangement = spacedBy(10.dp)
                        ) {
                            listOf("대구광역시", "달서구").forEachIndexed { index, category ->
                                SsaviceChip(
                                    text = category,
                                    selected = form.selectedCategory == index,
                                    onSelectedChange = { onSearchRangeChange(index) },
                                )
                            }
                        }
                    }

                    InnerFieldWithLabel("가격") {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text(text = "최소", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedTextField(
                                value = formatPrice(form.priceRange.first),
                                maxLines = 1,
                                modifier = Modifier.weight(1f),
                                onValueChange = {
                                    val new = min(
                                        min(
                                            max(it.filter { it.isDigit() }.toIntOrNull() ?: 0, 0),
                                            10_000_000
                                        ),
                                        form.priceRange.last
                                    )
                                    onPriceRangeChange(new..form.priceRange.last)
                                }
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "원", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(200.dp),
                        ) {
                            Text(text = "최대", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(10.dp))
                            OutlinedTextField(
                                value = formatPrice(form.priceRange.last),
                                maxLines = 1,
                                modifier = Modifier.weight(1f),
                                onValueChange = {
                                    val new = max(
                                        min(
                                            max(it.filter { it.isDigit() }.toIntOrNull() ?: 0, 0),
                                            10_000_000
                                        ),
                                        form.priceRange.first
                                    )
                                    onPriceRangeChange(form.priceRange.first..new)
                                }
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
                            verticalArrangement = spacedBy(5.dp)
                        ) {
                            listOf(
                                "인기순",
                                "높은 가격순",
                                "낮은 가격순",
                                "할인율순",
                                "마감 임박순"
                            ).forEachIndexed { i, s ->
                                SsaviceChip(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    text = s,
                                    selected = form.sortBy.value == i,
                                    onSelectedChange = { onSortByChange(SortingOrder.entries[i]) },
                                    innerPadding = PaddingValues(vertical = 10.dp)
                                )

                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatPrice(price: Int): String {
    return String.format("%,d", price)
}

@Composable
private fun InnerFieldWithLabel(label: String, content: @Composable () -> Unit) {
    Text(
        modifier = Modifier.padding(bottom = 12.dp),
        text = label,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold
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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(),
                form = SearchForm(
                    query = query.text.toString(),
                    categories = listOf("전체", "운동/피트니스", "교육/학습", "쇼핑/공동구매", "생활/취미"),
                    selectedCategory = 0,
                    searchRange = 1,
                    priceRange = priceRange,
                    sortBy = SortingOrder.POPULARITY,
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
