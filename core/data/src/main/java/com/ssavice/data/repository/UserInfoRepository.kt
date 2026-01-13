package com.ssavice.data.repository

import com.ssavice.model.service.ServiceState
import com.ssavice.model.service.SortingOrder
import com.ssavice.model.user.ParticipationSummary
import com.ssavice.model.user.UserProfile
import com.ssavice.model.user.UserProfileUpdateForm
import com.ssavice.model.user.UserServiceParticipation

interface UserInfoRepository {
    suspend fun getUserParticipationSummary(): Result<ParticipationSummary>

    suspend fun getUserProfile(): Result<UserProfile>

    suspend fun getMyService(
        searchCount: Int,
        page: Int?,
        sortingOrder: SortingOrder,
        serviceState: ServiceState,
    ): Result<UserServiceParticipation>

    suspend fun updateUserProfile(profile: UserProfileUpdateForm): Result<Unit>
}
