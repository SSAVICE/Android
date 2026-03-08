package com.ssavice.data.repository

import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.RegionDetail
import com.ssavice.model.RegionInfo
import com.ssavice.model.ResizableImage
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.service.WishList
import com.ssavice.model.user.ParticipationSummary
import com.ssavice.model.user.UserProfile
import com.ssavice.model.user.UserProfileUpdateForm
import com.ssavice.model.user.UserServiceParticipation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UserInfoRepository {
    suspend fun getUserParticipationSummary(): Result<ParticipationSummary>

    fun getUserProfile(): StateFlow<UserProfile>

    suspend fun getMyService(
        searchCount: Int,
        page: Int?,
        sortingOrder: SortingOrder,
        serviceState: ServiceState,
    ): Result<UserServiceParticipation>

    suspend fun updateUserProfile(profile: UserProfileUpdateForm): Result<Unit>

    fun updateUserProfileImage(image: ResizableImage): Flow<ImageUploadProgress>

    suspend fun getUserAddress(): Result<RegionDetail>

    suspend fun updateUserAddress(region: RegionInfo): Result<Unit>

    suspend fun wishService(
        id: Long,
        toEnable: Boolean,
    ): Result<Unit>

    suspend fun getWishList(
        searchCount: Int,
        page: Int?,
    ): Result<WishList>

    suspend fun unregisterAccount(): Result<Unit>
}
