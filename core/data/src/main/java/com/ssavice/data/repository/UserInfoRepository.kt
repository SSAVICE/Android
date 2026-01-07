package com.ssavice.data.repository

import com.ssavice.model.user.ParticipationSummary
import com.ssavice.model.user.UserProfile

interface UserInfoRepository {
    suspend fun getUserParticipationSummary(): Result<ParticipationSummary>

    suspend fun getUserProfile(): Result<UserProfile>
}
