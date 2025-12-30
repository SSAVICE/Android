package com.ssavice.data.repository

import com.ssavice.model.Seller.SellerRegisterForm
import com.ssavice.model.Seller.SellerMainInfo
import kotlinx.coroutines.flow.Flow

interface SellerInfoRepository {
    suspend fun registerSellerInformation(sellerInfo: SellerRegisterForm): Result<Unit>

    fun getMySellerInformation(): Flow<Result<SellerMainInfo>>
}
