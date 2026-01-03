package com.ssavice.search

import com.ssavice.model.service.SortingOrder

data class SearchFormUiState(
    val form: SearchForm
)

data class SearchForm(
    val query: String,
    val categories: List<String>,
    val selectedCategory: Int,
    val searchRange: Int,
    val priceRange: IntRange,
    val sortBy: SortingOrder,
)
