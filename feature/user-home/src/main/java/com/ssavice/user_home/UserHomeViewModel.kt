package com.ssavice.user_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.enums.Category
import com.ssavice.model.RegionInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeViewModel
    @Inject
    constructor(
        private val userRepository: UserInfoRepository,
    ) : ViewModel() {
        private val _uiState =
            MutableStateFlow(
                UserHomeUiState(
                    categories = Category.entries,
                ),
            )
        val uiState: StateFlow<UserHomeUiState> = _uiState

        fun onCategorySelect(index: Int) {
            if ((index !in 0 until _uiState.value.categories.size) || index == _uiState.value.selected) return
            _uiState.value =
                _uiState.value.copy(
                    selected = index,
                    defaultSearchQuery =
                        _uiState.value.defaultSearchQuery.copy(
                            category = _uiState.value.categories[index],
                        ),
                )
        }

        fun onNotificationButtonClick() {
        }

        fun onSetLocationClick() {
            showAddressSelector()
        }

        fun initUserAddress() {
            if (_uiState.value.addressState is RegionState.Loading) return

            _uiState.update {
                it.copy(
                    addressState = RegionState.Loading,
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.getUserAddress().fold(
                    onSuccess = { address ->
                        _uiState.update {
                            it.copy(
                                addressState =
                                    RegionState.Showing(
                                        address = address.regionInfo.address,
                                        detailAddress = address.regionInfo.detailAddress,
                                        latitude = address.regionInfo.latitude,
                                        longitude = address.regionInfo.longitude,
                                        postCode = address.regionInfo.postCode,
                                        regionCode = address.regionInfo.regionCode,
                                    ),
                            )
                        }
                    },
                    onFailure = { e ->
                        _uiState.update {
                            it.copy(
                                addressState = RegionState.Error(e),
                            )
                        }
                    },
                )
            }
        }

        fun updateUserAddress(address: RegionState.Showing) {
            _uiState.update {
                it.copy(
                    addressState = RegionState.Loading,
                    showAddressPicker = false,
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                userRepository
                    .updateUserAddress(
                        RegionInfo(
                            address = address.address,
                            detailAddress = address.detailAddress,
                            latitude = address.latitude,
                            longitude = address.longitude,
                            postCode = address.postCode,
                            regionCode = address.regionCode,
                        ),
                    ).fold(
                        onSuccess = {
                            _uiState.update {
                                it.copy(
                                    addressState = RegionState.Initial,
                                )
                            }
                            initUserAddress()
                        },
                        onFailure = { e ->
                            _uiState.update {
                                it.copy(
                                    addressState = RegionState.Error(e),
                                )
                            }
                        },
                    )
            }
        }

        private fun showAddressSelector() {
            _uiState.update {
                it.copy(
                    showAddressPicker = true,
                )
            }
        }

        fun onAddressSelectorDismiss() {
            _uiState.update {
                it.copy(
                    showAddressPicker = false,
                )
            }
        }
    }
