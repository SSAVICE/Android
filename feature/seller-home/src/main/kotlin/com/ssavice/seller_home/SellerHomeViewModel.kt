package com.ssavice.seller_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.Date
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerHomeViewModel
@Inject
constructor(
    private val repository: SellerInfoRepository,
) : ViewModel() {
    val uiState: StateFlow<SellerHomeUiState> by lazy {
        val mf = MutableStateFlow<SellerHomeUiState>(SellerHomeUiState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            sellerInfoFlow.collect { sellerInfo ->
                mf.update {
                    SellerHomeUiState.Shown(
                        sellerInfo.services.map { service ->
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
                }
            }
        }
        mf
    }
    val sellerInfoFlow by lazy {
        repository.getMySellerInformation()
    }
}
