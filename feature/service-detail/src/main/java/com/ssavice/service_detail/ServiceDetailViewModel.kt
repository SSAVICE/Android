package com.ssavice.service_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.service_detail.navigation.ServiceDetailRouteContract
import com.ssavice.service_detail.ui.seller.ParticipantUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServiceDetailViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle,
    private val serviceRepository: ServiceRepository,
    private val sellerRepository: SellerInfoRepository,
    private val userRepository: UserInfoRepository,
) : ViewModel() {
    private val _sellerId = MutableStateFlow(-1L)
    val sellerId = _sellerId
    private val _uiState =
        MutableStateFlow(
            ServiceDetailUiState(
                serviceInfoState = InfoState.Initial,
                sellerInfoState = InfoState.Initial,
            ),
        )

    val uiState = _uiState

    private fun getParameterFromSavedStateHandle(): Pair<Long, Boolean>? {
        val id = savedStateHandle.get<Long>(ServiceDetailRouteContract.ID)
        val isSeller = savedStateHandle.get<Boolean>(ServiceDetailRouteContract.IS_SELLER)

        if (id != null && isSeller != null) {
            return Pair(id, isSeller)
        }
        return null
    }

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

    fun onChatButtonClick() {
        _uiState.update { state ->
            if (state.serviceInfoState is InfoState.Done) {
                val id = state.service?.companyId ?: return
                state.copy(
                    serviceInfoState = InfoState.StartChat(
                        id = id
                    )
                )
            } else state
        }
    }

    fun onChatToUserButtonClick(userId: Long) {
        _uiState.update { state ->
            state.copy(
                serviceInfoState = InfoState.StartChat(
                    id = userId
                )
            )
        }
    }

    fun onInit() {
        val params = getParameterFromSavedStateHandle()

        if (params != null) {
            val id = params.first
            val isSeller = params.second

            loadService(id, isSeller)

            if (isSeller) {
                loadParticipants(id)
            }
        }
    }

    private fun loadService(
        id: Long,
        isSeller: Boolean = false,
    ) {
        _uiState.value =
            _uiState.value.copy(
                serviceInfoState = InfoState.Loading,
                showSellerInfo = isSeller,
                showUserInfo = !isSeller,
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
                                    address = "${it.regionInfo.region1}, ${it.regionInfo.region2}",
                                    description = it.description,
                                    tags = it.tag.split(','),
                                    liked = it.liked,
                                    applied = it.booked,
                                    latitude = it.regionInfo.latitude,
                                    longitude = it.regionInfo.longitude,
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

    private fun loadParticipants(id: Long) {
        viewModelScope.launch {
            val participants = serviceRepository.getServiceParticipant(id, 5, 0)
            participants.onSuccess { result ->
                _uiState.update {
                    it.copy(
                        accountInfo =
                            SellerAccountInfo(
                                expectedRevenue = 100,
                                participantCount = result.size,
                                pricePerPerson = 100,
                                lastNotice = "2/15 집합 장소가 변경되었습니다",
                                noticeDate = "02.08",
                                participants =
                                    result.items.map { item ->
                                        ParticipantUiModel(
                                            profileUrl = item.thumbnailUrl,
                                            userId = item.userId,
                                            name = item.name,
                                        )
                                    },
                            ),
                    )
                }
            }
        }
    }

    fun onParticipateButtonClick() {
        _uiState.update { it.copy(applyInfoState = InfoState.Loading) }
        _uiState.value.service?.run {
            viewModelScope.launch(Dispatchers.IO) {
                serviceRepository
                    .applyService(id)
                    .onSuccess {
                        _uiState.update {
                            it.copy(
                                applyInfoState = InfoState.Done,
                            )
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(
                                applyInfoState = InfoState.Error(e),
                            )
                        }
                    }
            }
        } ?: run {
            Log.d("KSC", "service is null")
        }
    }

    fun onLikeButtonClick(id: Long) {
        if (_uiState.value.serviceLikeState == InfoState.Loading) return
        if (_uiState.value.service != null) {
            val isLiked = (uiState.value.service?.liked) ?: return
            _uiState.update {
                it.copy(
                    serviceLikeState = InfoState.Loading,
                    service =
                        it.service?.copy(
                            liked = !isLiked,
                        ),
                )
            }
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.wishService(id, !isLiked).fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(serviceLikeState = InfoState.Done)
                        }
                    },
                    onFailure = { e ->
                        _uiState.update {
                            it.copy(
                                serviceLikeState = InfoState.Error(e),
                                service =
                                    it.service?.copy(
                                        liked = isLiked,
                                    ),
                            )
                        }
                    },
                )
            }
        }
    }

    fun onShareButtonClick() {
    }
}
