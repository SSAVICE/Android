package com.ssavice.seller_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.Date
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SellerHomeViewModel
    @Inject
    constructor(
        private val repository: SellerInfoRepository,
    ) : ViewModel() {
        val uiState: StateFlow<SellerHomeUiState> =
            repository
                .getMySellerInformation()
                .map {
                    it.fold(
                        onSuccess = { info ->
                            SellerHomeUiState.Shown(
                                info.services.map { service ->
                                    SellerItemUiState(
                                        id = service.id,
                                        title = service.name,
                                        category = service.category,
                                        meta = "${service.currentMember}명",
                                        priceText = "₩${service.discountedPrice}",
                                        isRecruiting = service.deadLine > Date.now(),
                                        imageUrl = service.image,
                                        state = service.state,
                                    )
                                },
                            )
                        },
                    ) { e ->
                        SellerHomeUiState.Error(e.message ?: "")
                    }
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = SellerHomeUiState.Loading,
                )
    }
