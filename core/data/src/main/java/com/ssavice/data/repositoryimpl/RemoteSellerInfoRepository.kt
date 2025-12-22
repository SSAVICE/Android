package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.model.SellerInfo
import com.ssavice.network.model.AddCompanyDTO
import com.ssavice.network.processResponse
import javax.inject.Inject

internal class RemoteSellerInfoRepository @Inject constructor(
    private val companyRetrofitService: CompanyRetrofitService
) : SellerInfoRepository {
    override suspend fun registerSellerInformation(sellerInfo: SellerInfo): Result<Unit> {
        return processResponse(
            companyRetrofitService.registerSeller(
                AddCompanyDTO.fromModel(sellerInfo)
            )
        )
    }
}
