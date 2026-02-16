package com.ssavice.data.repositoryimpl

import android.util.Log
import com.ssavice.data.repository.SellerInfoRepository
import com.ssavice.data.service.CompanyRetrofitService
import com.ssavice.data.service.ImageUploadService
import com.ssavice.model.Date
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.Region
import com.ssavice.model.RegionDetail
import com.ssavice.model.RegionInfo
import com.ssavice.model.ResizableImage
import com.ssavice.model.auth.CompanyVerifyToken
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.seller.SellerDetail
import com.ssavice.model.seller.SellerMainInfo
import com.ssavice.model.seller.SellerProfileUpdateForm
import com.ssavice.model.seller.SellerRegisterForm
import com.ssavice.model.seller.SellerReviews
import com.ssavice.model.seller.SellerServiceParticipation
import com.ssavice.model.seller.SellerSummary
import com.ssavice.model.user.ParticipationSummary
import com.ssavice.network.ProgressRequestBody
import com.ssavice.network.model.ConfirmImageDTO
import com.ssavice.network.model.ContentTypeDTO
import com.ssavice.network.model.company.AddCompanyDTO
import com.ssavice.network.model.company.UpdateCompanyProfileDTO
import com.ssavice.network.model.company.ValidateBusinessDTO
import com.ssavice.network.processResponse
import com.ssavice.network.processResponseOnResponseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject

internal class RemoteSellerInfoRepository
    @Inject
    constructor(
        private val companyRetrofitService: CompanyRetrofitService,
        private val imageUploadService: ImageUploadService,
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

        override suspend fun getSellerAddress(): Result<RegionDetail> =
            processResponseOnResponseData(
                companyRetrofitService.getCompanyAddress(),
            ).map {
                RegionDetail(
                    regionInfo =
                        RegionInfo(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            address = it.address,
                            detailAddress = it.detailAddress,
                            postCode = it.postCode,
                            regionCode = it.regionCode,
                        ),
                    region1 = it.gugun,
                    region2 = it.region,
                )
            }

        override suspend fun getSellerParticipationSummary(): Result<ParticipationSummary> =
            processResponseOnResponseData(
                companyRetrofitService.getCompanyParticipationSummary(),
            ).map { it.toModel() }

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

        override fun updateSellerProfileImage(image: ResizableImage): Flow<ImageUploadProgress> =
            channelFlow {
                Log.d("KSC", "updateSellerProfileImage: ${image.mimeType}")
                send(
                    ImageUploadProgress.Preprocessing,
                )

                val fetchUrlRequest =
                    processResponseOnResponseData(
                        companyRetrofitService.requestProfileUploadUrl(
                            ContentTypeDTO(
                                contentType = image.mimeType,
                            ),
                        ),
                    )

                val body =
                    ProgressRequestBody(
                        contentType = image.mimeType.toMediaTypeOrNull(),
                        data = image.data,
                        onProgress = { progress ->
                            trySend(ImageUploadProgress.Progress((progress * 100).toInt()))
                        },
                    )

                fetchUrlRequest
                    .onFailure {
                        send(ImageUploadProgress.Error(it))
                    }.onSuccess { url ->
                        val imageResponse =
                            imageUploadService.uploadImage(
                                url = url.uploadUrl,
                                contentType = image.mimeType,
                                body = body,
                            )
                        send(ImageUploadProgress.Progress(0))
                        processResponse(imageResponse)
                            .onSuccess { key ->
                                send(ImageUploadProgress.Progress(100))

                                processResponse(
                                    companyRetrofitService.confirmProfileUpload(
                                        body =
                                            ConfirmImageDTO(
                                                objectKey = url.objectKey,
                                            ),
                                    ),
                                ).onSuccess { _ ->
                                    send(ImageUploadProgress.Done(url.objectKey))
                                    getMySellerInformation()
                                }.onFailure { e ->
                                    send(ImageUploadProgress.Error(e))
                                }
                            }.onFailure { e ->
                                send(ImageUploadProgress.Error(e))
                            }
                    }
            }

        override suspend fun getSellerDetail(id: Long): Result<SellerDetail> =
            processResponseOnResponseData(
                companyRetrofitService.getCompanyDetail(id),
            ).map {
                it.toModel()
            }

        override suspend fun getSellerReviews(
            id: Long,
            page: Int,
            searchCount: Int,
        ): Result<SellerReviews> =
            processResponseOnResponseData(
                companyRetrofitService.getCompanyReview(
                    id = id,
                    page = page,
                    size = searchCount,
                ),
            ).map {
                it.toModel()
            }

        override suspend fun updateSellerProfile(profile: SellerProfileUpdateForm): Result<Unit> =
            processResponse(
                companyRetrofitService.putCompanyProfile(
                    body = UpdateCompanyProfileDTO.fromModel(profile),
                ),
            ).onSuccess {
                getMySellerInformation()
                Unit
            }
    }
