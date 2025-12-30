package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.network.model.AddCompanyDTO
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
        override suspend fun registerSellerInformation(sellerInfo: SellerRegisterForm): Result<Unit> =
            processResponse(
                companyRetrofitService.registerSeller(
                    AddCompanyDTO.fromModel(sellerInfo),
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
    }
