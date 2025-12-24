package com.ssavice.seller_main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssavice.data.repository.SellerInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SellerMainViewModel @Inject constructor(
    private val repository: SellerInfoRepository
): ViewModel() {
    val uiState: StateFlow<SellerMainUiState> =
        repository.getSellerInformation(0L).map {
            it.fold(
                onSuccess = { info ->
                    SellerMainUiState.Shown(
                        info.services.map { service ->
                            SellerItemUiState(
                                id = service.id,
                                title = service.name,
                                category = service.serviceTag[0],
                                meta = "${service.currentMember}명",
                                priceText = "₩${service.discountedPrice}",
                                isRecruiting = service.deadLine.isAfter(LocalDateTime.now()) ,
                                imageUrl = service.image,
                            )
                        }
                    )
                }
            ){
                e ->
                SellerMainUiState.Error(e.message?:"")
            }
        }.onStart {
            emit(SellerMainUiState.Loading)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SellerMainUiState.Loading
        )
}
