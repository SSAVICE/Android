package com.ssavice.user_my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.network.authentication.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMyPageViewModel
    @Inject
    constructor(
        private val userInfoRepository: UserInfoRepository,
        private val authRepository: AuthenticationRepository,
    ) : ViewModel() {
        private val _uiState by lazy {
            val mf =
                MutableStateFlow(
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
                                ),
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

        fun onLogout() {
            viewModelScope.launch(Dispatchers.IO) {
                authRepository
                    .logout()
                    .onSuccess {
                        _uiState.value =
                            _uiState.value.copy(
                                participationState = MyPageState.Done,
                            )
                        userInfoRepository.getUserAddress()
                    }
            }
        }
    }
