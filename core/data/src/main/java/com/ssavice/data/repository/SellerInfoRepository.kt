package com.ssavice.data.repository

import com.ssavice.model.SellerInfo

interface SellerInfoRepository {
    suspend fun registerSellerInformation(
        sellerInfo: SellerInfo
    ): Result<Unit>
}
