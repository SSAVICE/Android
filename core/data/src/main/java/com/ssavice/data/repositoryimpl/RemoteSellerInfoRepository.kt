package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.model.SellerInfo
import com.ssavice.model.SellerMainInfo
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
    override suspend fun registerSellerInformation(sellerInfo: SellerInfo): Result<Unit> =
        processResponse(
            companyRetrofitService.registerSeller(
                AddCompanyDTO.fromModel(sellerInfo),
            ),
        )

    override fun getSellerInformation(sellerId: Long): Flow<Result<SellerMainInfo>> {
        return flow {
            emit(
                processResponseOnResponseData(
                    companyRetrofitService.getCompanyInfo(sellerId)
                ).map {
                    it.toSellerMainInfoModel()
                }
            )
        }
    }
}
