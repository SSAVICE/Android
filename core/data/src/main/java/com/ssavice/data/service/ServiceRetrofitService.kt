package com.ssavice.data.service

import com.ssavice.network.model.ImageUploadDTO
import com.ssavice.network.model.PresignedUrlResponseDTO
import com.ssavice.network.model.review.PostReviewDTO
import com.ssavice.network.model.service.AddServiceDTO
import com.ssavice.network.model.service.AddServiceResponseDTO
import com.ssavice.network.model.service.ApplyServiceResultDTO
import com.ssavice.network.model.service.GetServiceDetailDTO
import com.ssavice.network.model.service.GetServiceParticipantDTO
import com.ssavice.network.model.service.SearchServiceResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ServiceRetrofitService {
    @POST("/api/service")
    suspend fun postService(
        @Body body: AddServiceDTO,
    ): Result<AddServiceResponseDTO>

    @GET("/api/service/search")
    suspend fun searchService(
        @QueryMap options: Map<String, String>,
    ): Result<SearchServiceResponseDTO>

    @GET("/api/service/{id}")
    suspend fun getService(
        @Path(value = "id") id: Long,
    ): Result<GetServiceDetailDTO>

    @POST("/api/service/image")
    suspend fun requestServiceImageUploadUrl(
        @Body contentTypes: ImageUploadDTO,
    ): Result<PresignedUrlResponseDTO>

    @POST("/api/book/{id}/apply")
    suspend fun applyService(
        @Path(value = "id") id: Long,
    ): Result<ApplyServiceResultDTO>

    @POST("/api/book/{id}/cancel")
    suspend fun cancelService(
        @Path(value = "id") id: Long,
    ): Result<Unit>

    @POST("/api/review")
    suspend fun postReview(
        @Body body: PostReviewDTO,
    ): Result<Unit>

    @DELETE("/api/service/{id}")
    suspend fun deleteService(
        @Path(value = "id") id: Long,
    ): Result<Unit>

    @GET("/api/book/book/{id}/participant")
    suspend fun getParticipant(
        @Path(value = "id") id: Long,
        @Query("size") size: Int,
        @Query("page") page: Int,
    ): Result<GetServiceParticipantDTO>
}
