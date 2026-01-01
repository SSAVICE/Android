package com.ssavice.user_main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserMainViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(UserMainUiState(
        categories = listOf("요가", "헬스", "필라테스", "건강", "쇼핑", "취미", "문화")
    ))
    val uiState: StateFlow<UserMainUiState> = _uiState

    fun onCategorySelect(index: Int) {
        if((index !in 0 until _uiState.value.categories.size) || index == _uiState.value.selected) return
        _uiState.value = _uiState.value.copy(
            selected = index,
            defaultSearchQuery = _uiState.value.defaultSearchQuery.copy(
                category = _uiState.value.categories[index]
            )
        )
    }
}
