package com.ssavice.search

import com.ssavice.model.enums.SortingOrder

data class SearchFormUiState(
    val form: SearchForm,
    val region1String: String,
    val region2String: String
)

data class SearchForm(
    val query: String,
    val categories: List<String>,
    val selectedCategory: Int,
    val searchRange: Int,
    val priceRange: IntRange,
    val sortBy: SortingOrder,
)
