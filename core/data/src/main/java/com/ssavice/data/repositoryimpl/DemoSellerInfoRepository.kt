package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.Date
import com.ssavice.model.RegionInfo
import com.ssavice.model.Review
import com.ssavice.model.auth.CompanyVerifyToken
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerSummary
import com.ssavice.model.service.ServiceSummary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Thread.sleep
import java.net.URL
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

internal class DemoSellerInfoRepository
    @Inject
    constructor() : SellerInfoRepository {
        override suspend fun registerSellerInformation(sellerInfo: SellerRegisterForm): Result<Unit> {
            delay(500)
            return Result.success(Unit)
        }

        override fun getMySellerInformation(): Flow<Result<SellerMainInfo>> {
            val rand = Random(LocalDateTime.now().second)

            fun createService(
                name: String,
                tags: List<String> = listOf("A", "B", "C"),
            ) = ServiceSummary(
                id = rand.nextLong(),
                name = name,
                image =
                    URL(
                        "https://images.unsplash.com/photo-1766047125728-ebff5afcf314?q=80&w=1760&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    ),
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
            )

            return flow {
                delay(1000)
                emit(
                    Result.success(
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
                            region = RegionInfo.demo,
                        ),
                    ),
                )
            }
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
                            Review(userName = "권*찬", comment = "너무 좋아요", serviceName = "요가 클래스", createdAt = Date.now(), rating = 4),
                            Review(
                                userName = "장*욱",
                                comment = "사장님이 친절해요 \n서비스 퀄리티도 좋아요",
                                serviceName = "요가 클래스",
                                createdAt = Date.now(),
                                rating = 5,
                            ),
                            Review(userName = "추*훈", comment = "별로임", serviceName = "요가 클래스", createdAt = Date.now(), rating = 2),
                        ),
                ),
            )
        }

    override suspend fun verifyBusinessInfo(
        name: String,
        openDate: Date,
        businessNumber: String
    ): Result<CompanyVerifyToken> {
        return Result.success(CompanyVerifyToken("token", System.currentTimeMillis()))
    }
}
