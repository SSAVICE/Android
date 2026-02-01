package com.ssavice.user_my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMyPageViewModel @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    private val _uiState by lazy {
        val mf = MutableStateFlow(
            MyPageUiState(),
        )

        viewModelScope.launch {
            userInfoFlow.collect { profile ->
                mf.update {
                    it.copy(
                        profile =
                            ProfileState(
                                name = profile.name,
                                locationInfo = profile.address,
                                description = "",
                                createdAt = profile.createdAt.toSimpleString(),
                                profileUrl = profile.imageUrl,
                                email = profile.email,
                                phoneNumber = profile.phoneNumber,
                            )
                    )
                }
            }
        }
        mf
    }
    val uiState by lazy { _uiState.asStateFlow() }
    val userInfoFlow by lazy {
        userInfoRepository.getUserProfile()
    }


    fun loadProfile() {
        _uiState.value =
            _uiState.value.copy(
                profileState = MyPageState.Loading,
            )
    }

    fun loadParticipationInfo() {
        _uiState.value =
            _uiState.value.copy(
                participationState = MyPageState.Loading,
            )

        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository.getUserParticipationSummary().fold(
                onSuccess = {
                    _uiState.update { currentState ->
                        currentState.copy(
                            participation =
                                ParticipationState(
                                    onProgress = it.onProgress,
                                    done = it.done,
                                    total = it.total,
                                ),
                            participationState = MyPageState.Done,
                        )
                    }
                },
                onFailure = {
                    _uiState.value =
                        _uiState.value.copy(
                            participationState = MyPageState.Error(it),
                        )
                },
            )
        }
    }
}
