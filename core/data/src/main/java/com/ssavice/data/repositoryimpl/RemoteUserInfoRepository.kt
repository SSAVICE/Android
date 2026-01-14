package com.ssavice.data.repositoryimpl

import android.util.Log
import com.ssavice.data.repository.UserInfoRepository
import com.ssavice.data.service.ImageUploadService
import com.ssavice.data.service.UserRetrofitInfoService
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.ResizableImage
import com.ssavice.model.service.ServiceState
import com.ssavice.model.service.SortingOrder
import com.ssavice.model.user.ParticipationSummary
import com.ssavice.model.user.UserProfile
import com.ssavice.model.user.UserProfileUpdateForm
import com.ssavice.model.user.UserServiceParticipation
import com.ssavice.network.ProgressRequestBody
import com.ssavice.network.model.ConfirmImageDTO
import com.ssavice.network.model.ContentTypeDTO
import com.ssavice.network.model.UpdateUserProfileDTO
import com.ssavice.network.processResponse
import com.ssavice.network.processResponseOnResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject

class RemoteUserInfoRepository
    @Inject
    constructor(
        private val userRetrofitService: UserRetrofitInfoService,
        private val imageUploadService: ImageUploadService,
    ) : UserInfoRepository {
        override suspend fun getUserParticipationSummary(): Result<ParticipationSummary> =
            processResponseOnResponseData(
                userRetrofitService.getUserParticipationSummary(),
            ).map { it.toModel() }

        override suspend fun getUserProfile(): Result<UserProfile> =
            processResponseOnResponseData(
                userRetrofitService.getUserProfile(),
            ).map {
                it.toModel()
            }

        override suspend fun getMyService(
            searchCount: Int,
            page: Int?,
            sortingOrder: SortingOrder,
            serviceState: ServiceState,
        ): Result<UserServiceParticipation> {
            TODO("Not yet implemented")
        }

        override suspend fun updateUserProfile(profile: UserProfileUpdateForm): Result<Unit> =
            processResponseOnResponseData(
                userRetrofitService.updateUserProfile(UpdateUserProfileDTO.fromModel(profile)),
            ).map { Unit }

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
                                }.onFailure { e ->
                                    send(ImageUploadProgress.Error(e))
                                }
                            }.onFailure { e ->
                                send(ImageUploadProgress.Error(e))
                            }
                    }
            }
    }
