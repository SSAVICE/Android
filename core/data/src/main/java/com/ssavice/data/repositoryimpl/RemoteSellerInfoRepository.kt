package com.ssavice.data.repositoryimpl

import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.model.Date
import com.ssavice.model.Region
import com.ssavice.model.auth.CompanyVerifyToken
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.seller.SellerProfileUpdateForm
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerServiceParticipation
import com.ssavice.model.seller.SellerSummary
import com.ssavice.model.user.ParticipationSummary
import com.ssavice.network.model.AddCompanyDTO
import com.ssavice.network.model.ValidateBusinessDTO
import com.ssavice.network.processResponse
import com.ssavice.network.processResponseOnResponseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class RemoteSellerInfoRepository
    @Inject
    constructor(
        private val companyRetrofitService: CompanyRetrofitService,
    ) : SellerInfoRepository {
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
        ): Result<Unit> =
            processResponse(
                companyRetrofitService.registerSeller(
                    AddCompanyDTO.fromModel(sellerInfo, token.token),
                ),
            )

        override fun getMySellerInformation(): StateFlow<SellerMainInfo> {
            CoroutineScope(Dispatchers.IO).launch {
                processResponseOnResponseData(
                    companyRetrofitService.getCompanyInfo(),
                ).map {
                    sellerInfoFlow.emit(it.toSellerMainInfoModel())
                }
            }
            return sellerInfoFlow
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

        override suspend fun getSellerParticipationSummary(): Result<ParticipationSummary> =
            processResponseOnResponseData(
                companyRetrofitService.getCompanyParticipationSummary(),
            ).map { it.toModel() }

        override suspend fun updateSellerProfile(profile: SellerProfileUpdateForm): Result<Unit> {
            TODO("Not yet implemented")
        }

        override suspend fun getMyService(
            searchCount: Int,
            page: Int?,
            sortingOrder: SortingOrder,
            serviceState: ServiceState,
        ): Result<SellerServiceParticipation> =
            processResponseOnResponseData(
                companyRetrofitService.getCompanyBook(
                    page = page ?: 0,
                    size = searchCount,
                    status = serviceState.name,
                ),
            ).map {
                it.toModel()
            }
    }
