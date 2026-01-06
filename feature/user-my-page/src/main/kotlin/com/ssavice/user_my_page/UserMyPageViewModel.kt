package com.ssavice.user_my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMyPageViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        MyPageUiState()
    )
    val uiState = _uiState.asStateFlow()

    fun loadProfile() {
        _uiState.value = _uiState.value.copy(
            profileState = MyPageState.Loading
        )

        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository.getUserProfile().fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        profile = ProfileState(
                            name = it.name,
                            locationInfo = it.address,
                            description = "",
                            createdAt = it.createdAt.toSimpleString(),
                            profileUrl = it.imageUrl
                        ),
                        profileState = MyPageState.Done
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        profileState = MyPageState.Error(it)
                    )
                }
            )
        }
    }

    fun loadParticipationInfo() {
        _uiState.value = _uiState.value.copy(
            participationState = MyPageState.Loading
        )

        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository.getUserParticipationSummary().fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        participation = ParticipationState(
                            onProgress = it.onProgress,
                            done = it.done,
                            total = it.total
                        ),
                        participationState = MyPageState.Done
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        participationState = MyPageState.Error(it)
                    )
                }
            )
        }
    }
}
