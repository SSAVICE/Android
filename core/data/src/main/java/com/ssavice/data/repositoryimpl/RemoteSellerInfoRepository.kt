package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.model.Date
import com.ssavice.model.auth.CompanyVerifyToken
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerSummary
import com.ssavice.network.model.AddCompanyDTO
import com.ssavice.network.model.ValidateBusinessDTO
import com.ssavice.network.processResponse
import com.ssavice.network.processResponseOnResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class RemoteSellerInfoRepository
    @Inject
    constructor(
        private val companyRetrofitService: CompanyRetrofitService,
    ) : SellerInfoRepository {
        override suspend fun registerSellerInformation(
            sellerInfo: SellerRegisterForm,
            token: CompanyVerifyToken,
        ): Result<Unit> =
            processResponse(
                companyRetrofitService.registerSeller(
                    AddCompanyDTO.fromModel(sellerInfo, token.token),
                ),
            )

        override fun getMySellerInformation(): Flow<Result<SellerMainInfo>> =
            flow {
                emit(
                    processResponseOnResponseData(
                        companyRetrofitService.getCompanyInfo(),
                    ).map {
                        it.toSellerMainInfoModel()
                    },
                )
            }

        override suspend fun getSellerSummary(id: Long): Result<SellerSummary> =
            processResponseOnResponseData(
                companyRetrofitService.getCompanySummary(id),
            ).map { it.toModel() }

        override suspend fun verifyBusinessInfo(
            name: String,
            openDate: Date,
            businessNumber: String,
        ): Result<CompanyVerifyToken> {
            val now = System.currentTimeMillis()
            val formattedDate = "%04d%02d%02d".format(openDate.year, openDate.month, openDate.day)
            return processResponseOnResponseData(
                companyRetrofitService.validateBusinessInfo(
                    ValidateBusinessDTO(
                        name = name,
                        startDate = formattedDate,
                        businessNumber = businessNumber,
                    ),
                ),
            ).map {
                CompanyVerifyToken(
                    it.verifyToken,
                    now,
                )
            }
        }
    }
