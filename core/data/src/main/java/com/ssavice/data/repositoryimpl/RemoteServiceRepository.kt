package com.ssavice.data.repositoryimpl

import android.util.Log
import com.ssavice.data.repository.ServiceRepository
import com.ssavice.data.service.ImageUploadService
import com.ssavice.data.service.ServiceRetrofitService
import com.ssavice.model.ImageUploadProgress
import com.ssavice.model.ResizableImage
import com.ssavice.model.service.ReviewForm
import com.ssavice.model.service.SearchQuery
import com.ssavice.model.service.SearchResult
import com.ssavice.model.service.ServiceAddForm
import com.ssavice.model.service.ServiceDetail
import com.ssavice.model.service.ServiceParticipantResponse
import com.ssavice.network.ProgressRequestBody
import com.ssavice.network.exception.ServerInternalErrorException
import com.ssavice.network.model.ContentTypeDTO
import com.ssavice.network.model.ImageUploadDTO
import com.ssavice.network.model.review.PostReviewDTO
import com.ssavice.network.model.service.AddServiceDTO
import com.ssavice.network.model.service.SearchServiceDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject

internal class RemoteServiceRepository
    @Inject
    constructor(
        private val serviceRetrofitService: ServiceRetrofitService,
        private val imageUploadService: ImageUploadService,
    ) : ServiceRepository {
        override suspend fun postService(service: ServiceAddForm): Result<Long> =
            serviceRetrofitService
                .postService(AddServiceDTO.fromModel(service, service.imageObjectKeys))
                .map { it.serviceId }

        override suspend fun searchService(
            query: SearchQuery,
            nextId: Long,
            searchCount: Int,
            startIndex: Int,
        ): Result<SearchResult> =
            serviceRetrofitService
                .searchService(
                    SearchServiceDTO
                        .fromModel(
                            query = query,
                            nextId = nextId,
                            searchCount = searchCount,
                        ).toMap(),
                ).map {
                    it.toModel()
                }

        override suspend fun searchService(
            query: SearchQuery,
            searchCount: Int,
            startIndex: Int,
        ): Result<SearchResult> =
            serviceRetrofitService
                .searchService(
                    SearchServiceDTO
                        .fromModel(
                            query = query,
                            nextId = null,
                            searchCount = searchCount,
                        ).toMap(),
                ).map {
                    it.toModel()
                }

        override suspend fun getService(id: Long): Result<ServiceDetail> = serviceRetrofitService.getService(id).map { it.toModel() }

        override fun addServiceImage(image: ResizableImage): Flow<ImageUploadProgress> =
            channelFlow {
                Log.d("KSC", "updateUserProfileImage: ${image.mimeType}")
                send(
                    ImageUploadProgress.Preprocessing,
                )

                val fetchUrlRequest =
                    serviceRetrofitService.requestServiceImageUploadUrl(
                        ImageUploadDTO(
                            add = listOf(ContentTypeDTO(contentType = image.mimeType)),
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
                        if (url.list.isEmpty()) {
                            send(
                                ImageUploadProgress.Error(ServerInternalErrorException("잘못된 반환 형식입니다")),
                            )
                            return@onSuccess
                        }
                        val imageResponse =
                            imageUploadService.uploadImage(
                                url = url.list[0].uploadUrl,
                                contentType = image.mimeType,
                                body = body,
                            )
                        send(ImageUploadProgress.Progress(0))
                        imageResponse
                            .onSuccess { key ->
                                send(ImageUploadProgress.Done(url.list[0].objectKey))
                            }.onFailure { e ->
                                send(ImageUploadProgress.Error(e))
                            }
                    }
            }

        override suspend fun applyService(id: Long): Result<Unit> = serviceRetrofitService.applyService(id).map {}

        override suspend fun cancelService(id: Long): Result<Unit> = serviceRetrofitService.cancelService(id)

        override suspend fun reviewService(review: ReviewForm): Result<Unit> =
            serviceRetrofitService.postReview(
                PostReviewDTO.fromModel(review),
            )

        override suspend fun deleteService(id: Long): Result<Unit> = serviceRetrofitService.deleteService(id)

        override suspend fun getServiceParticipant(
            id: Long,
            size: Int,
            page: Int,
        ): Result<ServiceParticipantResponse> =
            serviceRetrofitService
                .getParticipant(
                    id = id,
                    size = size,
                    page = page,
                ).map {
                    it.toModel()
                }
    }
