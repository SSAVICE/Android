package com.ssavice.seller_detail

import androidx.lifecycle.SavedStateHandle
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
class SellerDetailViewModel @Inject constructor(
    private val sellerRepository: SellerInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(SellerDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun load() {
        _uiState.update {
            it.copy(
                sellerDetailState = SellerDetailState.Loading
            )
        }

        val id = getIdFromSavedStateHandle()
        viewModelScope.launch(Dispatchers.IO) {
            fetchAndUpdateInfo(id)
        }

    }

    private fun getIdFromSavedStateHandle(): Long {
        return savedStateHandle.get<Long>("id") ?: -1L
    }

    private suspend fun fetchAndUpdateInfo(id: Long) {
        sellerRepository.getSellerDetail(id)
            .fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            sellerDetailState = SellerDetailState.Loaded,
                            sellerInfo = SellerInfoState(
                                name = it.sellerInfo.name,
                                description = it.sellerInfo.description,
                                detail = it.sellerInfo.detail,
                                address = it.sellerInfo.address,
                                detailAddress = it.sellerInfo.detailAddress,
                                phoneNumber = it.sellerInfo.phoneNumber,
                                imageUrls = it.sellerInfo.imageUrls,
                                id = it.sellerInfo.id,
                                region = it.sellerInfo.region,
                            ),
                            serviceItems = it.serviceItems,
                            reviewItems = it.reviewItems,
                            businessInfo = it.businessInfo
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            sellerDetailState = SellerDetailState.Error(e)
                        )
                    }
                }
            )
    }
}
