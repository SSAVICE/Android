package com.ssavice.data.repository

import com.ssavice.model.SellerInfo
import com.ssavice.model.SellerMainInfo
import kotlinx.coroutines.flow.Flow

interface SellerInfoRepository {
    suspend fun registerSellerInformation(sellerInfo: SellerInfo): Result<Unit>

    fun getSellerInformation(sellerId: Long): Flow<Result<SellerMainInfo>>
}
