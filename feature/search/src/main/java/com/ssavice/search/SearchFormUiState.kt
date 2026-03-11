package com.ssavice.search

import com.ssavice.model.enums.Category
import com.ssavice.model.enums.SearchRange
import com.ssavice.model.enums.SortingOrder

data class SearchFormUiState(
    val form: SearchForm,
    val region1String: String,
    val region2String: String,
)

data class SearchForm(
    val query: String,
    val categories: List<Category>,
    val selectedCategory: Category,
    val searchRange: SearchRange,
    val priceRange: IntRange,
    val sortBy: SortingOrder,
    val onSaleOnly: Boolean,
)
