package com.ssavice.service_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.service_detail.navigation.ServiceDetailRouteContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceDetailViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val serviceRepository: ServiceRepository,
    private val sellerRepository: SellerInfoRepository,
) : ViewModel() {
    private val _sellerId = MutableStateFlow(-1L)
    val sellerId = _sellerId
    val serviceId = savedStateHandle.getStateFlow(ServiceDetailRouteContract.ID, -1L)
    private val _uiState =
        MutableStateFlow(
            ServiceDetailUiState(
                serviceInfoState = InfoState.Waiting,
                sellerInfoState = InfoState.Waiting,
            ),
        )

    val uiState = _uiState

    fun loadSeller(id: Long) {
        _uiState.value =
            _uiState.value.copy(
                sellerInfoState = InfoState.Loading,
            )
        viewModelScope.launch(Dispatchers.IO) {
            sellerRepository.getSellerSummary(id).fold(
                onSuccess = {
                    _uiState.value =
                        _uiState.value.copy(
                            sellerInfoState = InfoState.Done,
                            seller =
                                SellerSummary(
                                    id = it.companyId,
                                    name = it.companyName,
                                    address = it.address,
                                    description = it.description,
                                    phoneNumber = it.phoneNumber,
                                    imageUrl = it.companyImageUrl ?: "",
                                    rate = it.companyRate,
                                    rateCount = it.rateCount,
                                    reviews =
                                        it.review.map { review ->
                                            Review(
                                                userName = review.userName,
                                                content = review.comment,
                                                rating = review.rating,
                                                createdAt = review.createdAt.toSimpleString(),
                                                serviceName = review.serviceName,
                                            )
                                        },
                                ),
                        )
                },
                onFailure = {
                    _uiState.value =
                        _uiState.value.copy(
                            sellerInfoState = InfoState.Error(it),
                        )
                },
            )
        }
    }

    fun loadService(id: Long) {
        _uiState.value =
            _uiState.value.copy(
                serviceInfoState = InfoState.Loading,
            )
        viewModelScope.launch(Dispatchers.IO) {
            serviceRepository.getService(id).fold(
                onSuccess = {
                    _uiState.value =
                        _uiState.value.copy(
                            serviceInfoState = InfoState.Done,
                            service =
                                ServiceDetail(
                                    deadLine = it.deadLine.toString(),
                                    startDate = it.startDate.toSimpleString(),
                                    endDate = it.endDate.toSimpleString(),
                                    imageUrls = it.imageUrls,
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
                                ),
                        )
                    _sellerId.value = it.companyId
                },
                onFailure = {
                    _uiState.value =
                        _uiState.value.copy(
                            serviceInfoState = InfoState.Error(it),
                        )
                },
            )
        }
    }

    fun onChatButtonClick() {

    }

    fun onParticipateButtonClick() {

    }

    fun onLikeButtonClick() {

    }

    fun onShareButtonClick() {

    }
}
