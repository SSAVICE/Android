package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.Date
import com.ssavice.model.Region
import com.ssavice.model.Review
import com.ssavice.model.auth.CompanyVerifyToken
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.seller.SellerProfileUpdateForm
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerServiceParticipation
import com.ssavice.model.seller.SellerSummary
import com.ssavice.model.service.ServiceSummary
import com.ssavice.model.user.ParticipationSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

internal class DemoSellerInfoRepository
    @Inject
    constructor() : SellerInfoRepository {
        private val sellerInfoFlow =
            MutableStateFlow(
                SellerMainInfo(
                    "",
                    "",
                    "",
                    "",
                    emptyList(),
                    Region(
                        0.0,
                        0.0,
                        "",
                        "",
                    ),
                    "",
                    "",
                    "",
                ),
            )

        override suspend fun registerSellerInformation(
            sellerInfo: SellerRegisterForm,
            token: CompanyVerifyToken,
        ): Result<Unit> {
            delay(500)
            return Result.success(Unit)
        }

        override fun getMySellerInformation(): StateFlow<SellerMainInfo> {
            val rand = Random(LocalDateTime.now().second)

            fun createService(
                name: String,
                tags: List<String> = listOf("A", "B", "C"),
            ) = ServiceSummary(
                id = rand.nextLong(),
                name = name,
                image = "https://picsum.photos/id/400",
                minimumMember = 20,
                currentMember = 10,
                basePrice = 50000,
                discountRatio = 10.0,
                discountedPrice = 45000,
                deadLine = Date.parse(LocalDateTime.now()),
                serviceTag = tags[0],
                startDate = Date.parse(LocalDateTime.now()),
                endDate = Date.parse(LocalDateTime.now()),
                category = "카테고리",
                state = ServiceState.RECRUITING,
            )

            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                sellerInfoFlow.emit(
                    SellerMainInfo(
                        companyName = "주식회사 싸비스",
                        phoneNumber = "010-1234-5678",
                        businessNumber = "123-45-67890",
                        description = "데모 판매자 정보입니다.",
                        services =
                            listOf(
                                createService("서비스1"),
                                createService("요가 클래스", listOf("힐링", "건강")),
                            ),
                        region = Region(0.0, 0.0, "달서구", "상인동"),
                        address = "대구 달서구 상인동",
                        detailAddress = "상인동 123-456",
                        imageUrl = "https://picsum.photos/400",
                    ),
                )
            }

            return sellerInfoFlow
        }

        override suspend fun getSellerSummary(id: Long): Result<SellerSummary> {
            delay((3..15).random() * 100L)
            return Result.success(
                SellerSummary(
                    companyId = id,
                    companyName = "주식회사 KSC",
                    address = "서울시 강남구 역삼동",
                    description = "최고의 서비스를 자랑하는 대한민국 명장 KSC입니다.\n전화 문의 운영 시간: 09:00 ~ 18:00\n언제든 편히 상담주세요.",
                    phoneNumber = "010-1234-5678",
                    companyImageUrl = "https://picsum.photos/id/$id/400",
                    companyRate = 4.5,
                    rateCount = 100,
                    review =
                        listOf(
                            Review(
                                userName = "권*찬",
                                comment = "너무 좋아요",
                                serviceName = "요가 클래스",
                                createdAt = Date.now(),
                                rating = 4,
                            ),
                            Review(
                                userName = "장*욱",
                                comment = "사장님이 친절해요 \n서비스 퀄리티도 좋아요",
                                serviceName = "요가 클래스",
                                createdAt = Date.now(),
                                rating = 5,
                            ),
                            Review(
                                userName = "추*훈",
                                comment = "별로임",
                                serviceName = "요가 클래스",
                                createdAt = Date.now(),
                                rating = 2,
                            ),
                        ),
                ),
            )
        }

        override suspend fun verifyBusinessInfo(
            name: String,
            openDate: Date,
            businessNumber: String,
        ): Result<CompanyVerifyToken> = Result.success(CompanyVerifyToken("token", System.currentTimeMillis()))

        override suspend fun getSellerParticipationSummary(): Result<ParticipationSummary> {
            TODO("Not yet implemented")
        }

        override suspend fun updateSellerProfile(profile: SellerProfileUpdateForm): Result<Unit> {
            TODO("Not yet implemented")
        }

        override suspend fun getMyService(
            searchCount: Int,
            page: Int?,
            sortingOrder: SortingOrder,
            serviceState: ServiceState,
        ): Result<SellerServiceParticipation> {
            TODO("Not yet implemented")
        }
    }
