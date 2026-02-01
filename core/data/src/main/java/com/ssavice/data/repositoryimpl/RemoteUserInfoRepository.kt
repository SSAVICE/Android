package com.ssavice.data.repositoryimpl

import android.util.Log
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.data.service.ImageUploadService
import com.ssavice.data.service.UserInfoRetrofitService
import com.ssavice.model.Date
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.RegionDetail
import com.ssavice.model.RegionInfo
import com.ssavice.model.ResizableImage
import com.ssavice.model.enums.ServiceState
import com.ssavice.model.enums.SortingOrder
import com.ssavice.model.user.ParticipationSummary
import com.ssavice.model.user.UserProfile
import com.ssavice.model.user.UserProfileUpdateForm
import com.ssavice.model.user.UserServiceParticipation
import com.ssavice.network.ProgressRequestBody
import com.ssavice.network.model.ConfirmImageDTO
import com.ssavice.network.model.ContentTypeDTO
import com.ssavice.network.model.RegionPostDTO
import com.ssavice.network.model.UpdateUserProfileDTO
import com.ssavice.network.processResponse
import com.ssavice.network.processResponseOnResponseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject
import kotlin.getValue

class RemoteUserInfoRepository
@Inject
constructor(
    private val userRetrofitService: UserInfoRetrofitService,
    private val imageUploadService: ImageUploadService,
) : UserInfoRepository {
    val userProfileFlow = MutableStateFlow(UserProfile(
        "",
        "",
        Date.now(),
        "",
        "",
        0,
        "",
        "")
    )
    override suspend fun getUserParticipationSummary(): Result<ParticipationSummary> =
        processResponseOnResponseData(
            userRetrofitService.getUserParticipationSummary(),
        ).map { it.toModel() }

    override fun getUserProfile(): StateFlow<UserProfile> {
        CoroutineScope(Dispatchers.IO).launch {
            processResponseOnResponseData(
                userRetrofitService.getUserProfile(),
            ).map {
                it.toModel()
            }.onSuccess {
                userProfileFlow.emit(it)
            }
        }
        return userProfileFlow
    }

    override suspend fun getMyService(
        searchCount: Int,
        page: Int?,
        sortingOrder: SortingOrder,
        serviceState: ServiceState,
    ): Result<UserServiceParticipation> =
        processResponseOnResponseData(
            userRetrofitService.getUserBook(
                page = page ?: 0,
                size = searchCount,
                status = serviceState.name,
            ),
        ).map {
            it.toModel()
        }

    override suspend fun updateUserProfile(profile: UserProfileUpdateForm): Result<Unit> =
        processResponseOnResponseData(
            userRetrofitService.updateUserProfile(UpdateUserProfileDTO.fromModel(profile)),
        ).map { newData ->
            userProfileFlow.update {
                it.copy(
                    name = newData.name,
                    email = newData.email,
                    phoneNumber = newData.phoneNumber,
                )
            }
            Unit
        }

    override fun updateUserProfileImage(image: ResizableImage): Flow<ImageUploadProgress> =
        channelFlow {
            Log.d("KSC", "updateUserProfileImage: ${image.mimeType}")
            send(
                ImageUploadProgress.Preprocessing,
            )

            val fetchUrlRequest =
                processResponseOnResponseData(
                    userRetrofitService.requestProfileUploadUrl(
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
                                userRetrofitService.confirmProfileUpload(
                                    body =
                                        ConfirmImageDTO(
                                            objectKey = url.objectKey,
                                        ),
                                ),
                            ).onSuccess { _ ->
                                send(ImageUploadProgress.Done(url.objectKey))
                                getUserProfile()
                            }.onFailure { e ->
                                send(ImageUploadProgress.Error(e))
                            }
                        }.onFailure { e ->
                            send(ImageUploadProgress.Error(e))
                        }
                }
        }

    override suspend fun getUserAddress(): Result<RegionDetail> =
        processResponseOnResponseData(
            userRetrofitService.getUserAddress(),
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

    override suspend fun updateUserAddress(region: RegionInfo): Result<Unit> =
        processResponse(
            userRetrofitService.updateUserAddress(
                RegionPostDTO.fromModel(region),
            ),
        )
}
