package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.model.SellerInfo
import com.ssavice.model.SellerMainInfo
import com.ssavice.network.model.AddCompanyDTO
import com.ssavice.network.processResponse
import com.ssavice.network.processResponseOnResponseData
import javax.inject.Inject

internal class RemoteSellerInfoRepository
    @Inject
    constructor(
        private val companyRetrofitService: CompanyRetrofitService,
    ) : SellerInfoRepository {
        override suspend fun registerSellerInformation(sellerInfo: SellerInfo): Result<Unit> =
            processResponse(
                companyRetrofitService.registerSeller(
                    AddCompanyDTO.fromModel(sellerInfo),
                ),
            )

    override suspend fun getSellerInformation(sellerId: Long): Result<SellerMainInfo> {
        return processResponseOnResponseData(
            companyRetrofitService.getCompanyInfo(sellerId)
        ).map {
            it.toSellerMainInfoModel()
        }
    }
}
