package com.ssavice.data.repository

import com.ssavice.model.Date
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.ResizableImage
import com.ssavice.model.auth.CompanyVerifyToken
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.seller.SellerProfileUpdateForm
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerServiceParticipation
import com.ssavice.model.seller.SellerSummary
import com.ssavice.model.user.ParticipationSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SellerInfoRepository {
    suspend fun registerSellerInformation(
        sellerInfo: SellerRegisterForm,
        token: CompanyVerifyToken,
    ): Result<Unit>

    fun getMySellerInformation(): StateFlow<SellerMainInfo>

    suspend fun getSellerSummary(id: Long): Result<SellerSummary>

    suspend fun verifyBusinessInfo(
        name: String,
        openDate: Date,
        businessNumber: String,
    ): Result<CompanyVerifyToken>

    suspend fun getSellerParticipationSummary(): Result<ParticipationSummary>

    suspend fun updateSellerProfile(profile: SellerProfileUpdateForm): Result<Unit>

    suspend fun getMyService(
        searchCount: Int,
        page: Int?,
        sortingOrder: SortingOrder,
        serviceState: ServiceState,
    ): Result<SellerServiceParticipation>

    fun updateSellerProfileImage(image: ResizableImage): Flow<ImageUploadProgress>
}
