package com.ssavice.data.repository

import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerMainInfo
import kotlinx.coroutines.flow.Flow

interface SellerInfoRepository {
    suspend fun registerSellerInformation(sellerInfo: SellerRegisterForm): Result<Unit>

    fun getMySellerInformation(): Flow<Result<SellerMainInfo>>
}
