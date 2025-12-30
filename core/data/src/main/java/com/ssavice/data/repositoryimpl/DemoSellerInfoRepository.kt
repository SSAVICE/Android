package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerMainInfo
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
            sleep(500)
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
                latitude = 0.0,
                longitude = 0.0,
                minimumMember = 20,
                currentMember = 10,
                basePrice = 50000,
                discountRatio = 10.0,
                discountedPrice = 45000,
                deadLine = LocalDateTime.now(),
                serviceTag = tags,
            )

            return flow {
                delay(1000)
                emit(
                    Result.success(
                        SellerMainInfo(
                            companyName = "주식회사 싸비스",
                            ownerName = "홍길동",
                            phoneNumber = "010-1234-5678",
                            businessNumber = "123-45-67890",
                            description = "데모 판매자 정보입니다.",
                            services =
                                listOf(
                                    createService("서비스1"),
                                    createService("요가 클래스", listOf("힐링", "건강")),
                                ),
                        ),
                    ),
                )
            }
        }
    }
