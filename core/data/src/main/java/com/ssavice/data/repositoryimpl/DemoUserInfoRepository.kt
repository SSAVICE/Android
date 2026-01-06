package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.Date
import com.ssavice.model.user.ParticipationSummary
import com.ssavice.model.user.UserProfile
import javax.inject.Inject

class DemoUserInfoRepository @Inject constructor(): UserInfoRepository {
    override suspend fun getUserParticipationSummary(): Result<ParticipationSummary> {
        return Result.success(
            ParticipationSummary(
                onProgress = 10,
                done = 21,
                total = 75
            )
        )
    }

    override suspend fun getUserProfile(): Result<UserProfile> {
        return Result.success(
            UserProfile(
                imageUrl = "https://picsum.photos/200",
                name = "권성찬",
                createdAt = Date.now(),
                email = "ksc1008@naver.com",
                phoneNumber = "010-1234-5678",
                postCode = 12354,
                address = "대구 달서구 송현동",
                detailAddress = "데모로 123",
            )
        )
    }
}
