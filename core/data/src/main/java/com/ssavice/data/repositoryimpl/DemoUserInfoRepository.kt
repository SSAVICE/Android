package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.model.Category
import com.ssavice.model.Date
import com.ssavice.model.service.ServiceState
import com.ssavice.model.service.SortingOrder
import com.ssavice.model.user.ParticipationSummary
import com.ssavice.model.user.UserProfile
import com.ssavice.model.user.UserProfileUpdateForm
import com.ssavice.model.user.UserServiceParticipation
import com.ssavice.model.user.UserServiceParticipationItem
import kotlinx.coroutines.delay
import javax.inject.Inject

class DemoUserInfoRepository
@Inject
constructor() : UserInfoRepository {
    override suspend fun getUserParticipationSummary(): Result<ParticipationSummary> =
        Result.success(
            ParticipationSummary(
                onProgress = 10,
                done = 21,
                total = 75,
            ),
        )

    override suspend fun getUserProfile(): Result<UserProfile> {
        delay(500)
        return Result.success(
            UserProfile(
                imageUrl = "https://picsum.photos/200",
                name = "권성찬",
                createdAt = Date.now(),
                email = "ksc1008@naver.com",
                phoneNumber = "01012345678",
                postCode = 12354,
                address = "대구 달서구 송현동",
                detailAddress = "데모로 123",
            ),
        )
    }

    private fun generateRandomService(
        id: Long,
        state: ServiceState,
    ): UserServiceParticipationItem {
        val startDayOffset = (0..6).random()
        return UserServiceParticipationItem(
            id = id,
            thumbnail = "https://picsum.photos/id/${id + 1}/400",
            category = Category.entries.random().name,
            price = (10000..100000).random(),
            name = "서비스 $id",
            sellerName = "판매자 $id",
            startDate = Date.now().addDay(startDayOffset),
            endDate = Date.now().addDay((0..6).random() + startDayOffset),
            state = state,
            isReviewed = (0..2).random() == 0,
        )
    }

    override suspend fun getMyService(
        searchCount: Int,
        page: Int?,
        sortingOrder: SortingOrder,
        serviceState: ServiceState,
    ): Result<UserServiceParticipation> {
        delay(1000)
        val hasMore = (page ?: 0) < 3
        val cnt = if (hasMore) searchCount else searchCount / 2

        return Result.success(
            UserServiceParticipation(
                items = List(cnt) { generateRandomService(it.toLong(), serviceState) },
                currentPage = (page ?: 0).toLong(),
                searchCount = cnt,
                hasNext = hasMore,
            ),
        )
    }

    override suspend fun updateUserProfile(profile: UserProfileUpdateForm): Result<Unit> {
        delay(100)
        return Result.success(Unit)
    }
}
