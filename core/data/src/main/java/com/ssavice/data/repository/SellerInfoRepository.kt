package com.ssavice.data.repository

import com.ssavice.model.SellerInfo
import com.ssavice.model.SellerMainInfo

interface SellerInfoRepository {
    suspend fun registerSellerInformation(sellerInfo: SellerInfo): Result<Unit>

    suspend fun getSellerInformation(sellerId: Long): Result<SellerMainInfo>
}
