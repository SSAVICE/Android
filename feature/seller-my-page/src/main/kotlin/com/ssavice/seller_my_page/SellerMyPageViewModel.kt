package com.ssavice.seller_my_page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerMyPageViewModel
    @Inject
    constructor(
        private val sellerInfoRepository: SellerInfoRepository,
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
                                    name = profile.companyName,
                                    locationInfo = profile.address,
                                    description = profile.description,
                                    profileUrl = profile.imageUrl,
                                    phoneNumber = profile.phoneNumber,
                                    detail = profile.detail,
                                ),
                        )
                    }
                }
            }
            mf
        }
        val uiState by lazy { _uiState.asStateFlow() }
        val userInfoFlow by lazy {
            sellerInfoRepository.getMySellerInformation()
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
                sellerInfoRepository.getSellerParticipationSummary().fold(
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
