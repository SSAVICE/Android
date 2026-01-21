package com.ssavice.data.repository

import com.ssavice.model.Date
import com.ssavice.model.auth.CompanyVerifyToken
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerSummary
import kotlinx.coroutines.flow.Flow

interface SellerInfoRepository {
    suspend fun registerSellerInformation(
        sellerInfo: SellerRegisterForm,
        token: CompanyVerifyToken,
    ): Result<Unit>

    fun getMySellerInformation(): Flow<Result<SellerMainInfo>>

    suspend fun getSellerSummary(id: Long): Result<SellerSummary>

    suspend fun verifyBusinessInfo(
        name: String,
        openDate: Date,
        businessNumber: String,
    ): Result<CompanyVerifyToken>
}
