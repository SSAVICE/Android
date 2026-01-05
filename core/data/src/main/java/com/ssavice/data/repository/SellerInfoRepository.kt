package com.ssavice.data.repository

import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerSummary
import kotlinx.coroutines.flow.Flow

interface SellerInfoRepository {
    suspend fun registerSellerInformation(sellerInfo: SellerRegisterForm): Result<Unit>

    fun getMySellerInformation(): Flow<Result<SellerMainInfo>>

    suspend fun getSellerSummary(id: Long): Result<SellerSummary>
}
