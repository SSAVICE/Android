package com.ssavice.service_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val serviceRepository: ServiceRepository,
    private val sellerRepository: SellerInfoRepository
): ViewModel() {
    private val _sellerId = MutableStateFlow(-1L)
    private val sellerId = _sellerId
        .map {
            if(it != -1L) {
                loadSeller(it)
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = -1
        )
    private val serviceId = savedStateHandle.getStateFlow("serviceId", -1L)
        .map {
            if(it != -1L) {
                loadSeller(it)
            }
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = -1
        )
    private val _uiState = MutableStateFlow(
        ServiceDetailUiState(
            serviceInfoState = InfoState.Waiting,
            sellerInfoState = InfoState.Waiting
        )
    )

    val uiState = _uiState

    private fun loadSeller(id: Long) {
        _uiState.value = _uiState.value.copy(
            sellerInfoState = InfoState.Loading
        )
        viewModelScope.launch(Dispatchers.IO) {

            sellerRepository.getSellerSummary(id).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        sellerInfoState = InfoState.Done,
                        seller = SellerSummary(
                            id = it.companyId,
                            name = it.companyName,
                            address = it.address,
                            description = it.description,
                            phoneNumber = it.phoneNumber,
                            imageUrl = it.companyImageUrl?:"",
                            rate = it.companyRate,
                            rateCount = it.rateCount,
                            reviews = it.review
                        )
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        sellerInfoState = InfoState.Error(it)
                    )
                }
            )
        }
    }

    private fun loadService(id: Long) {
        _uiState.value = _uiState.value.copy(
            serviceInfoState = InfoState.Loading
        )
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.getService(id).fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        serviceInfoState = InfoState.Done,
                        service = ServiceDetail(
                            deadLine = it.deadLine.toString(),
                            startDate = it.startDate.toSimpleString(),
                            endDate = it.endDate.toSimpleString(),
                            imageUrl = it.imageUrl,
                            basePrice = it.basePrice,
                            discountedPrice = it.discountedPrice,
                            discountRatio = it.discountRatio,
                            participantInfo = "${it.currentMember} (최소 인원: ${it.minimumMember})",
                            id = it.id,
                            companyId = it.companyId,
                            category = it.category,
                            name = it.name,
                            address = it.regionInfo.address,
                            description = it.description,
                            tags = it.tag.split(','),
                        )
                    )
                    _sellerId.value = it.companyId
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        serviceInfoState = InfoState.Error(it))
                })
        }
    }
}
