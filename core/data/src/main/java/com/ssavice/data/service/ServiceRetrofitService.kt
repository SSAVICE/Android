package com.ssavice.data.service

import com.ssavice.network.model.AddServiceDTO
import com.ssavice.network.model.AddServiceResponseDTO
import com.ssavice.network.model.ApplyServiceResultDTO
import com.ssavice.network.model.GetServiceDetailDTO
import com.ssavice.network.model.GetServiceParticipantDTO
import com.ssavice.network.model.ImageUploadDTO
import com.ssavice.network.model.PostReviewDTO
import com.ssavice.network.model.PresignedUrlResponseDTO
import com.ssavice.network.model.SearchServiceResponseDTO
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
    ): Response<AddServiceResponseDTO>

    @GET("/api/service/search")
    suspend fun searchService(
        @QueryMap options: Map<String, String>,
    ): Response<SearchServiceResponseDTO>

    @GET("/api/service/{id}")
    suspend fun getService(
        @Path(value = "id") id: Long,
    ): Response<GetServiceDetailDTO>

    @POST("/api/service/image")
    suspend fun requestServiceImageUploadUrl(
        @Body contentTypes: ImageUploadDTO,
    ): Response<PresignedUrlResponseDTO>

    @POST("/api/book/{id}/apply")
    suspend fun applyService(
        @Path(value = "id") id: Long,
    ): Response<ApplyServiceResultDTO>

    @POST("/api/book/{id}/cancel")
    suspend fun cancelService(
        @Path(value = "id") id: Long,
    ): Response<Unit>

    @POST("/api/review")
    suspend fun postReview(
        @Body body: PostReviewDTO,
    ): Response<Unit>

    @DELETE("/api/service/{id}")
    suspend fun deleteService(
        @Path(value = "id") id: Long,
    ): Response<Unit>

    @GET("/api/book/book/{id}/participant")
    suspend fun getParticipant(
        @Path(value = "id") id: Long,
        @Query("size") size: Int,
        @Query("page") page: Int,
    ): Response<GetServiceParticipantDTO>
}
