package com.ssavice.data.service

import com.ssavice.network.model.ConfirmImageDTO
import com.ssavice.network.model.ContentTypeDTO
import com.ssavice.network.model.PresignedUrlDTO
import com.ssavice.network.model.UpdateUserProfileDTO
import com.ssavice.network.model.UpdateUserProfileResponseDTO
import com.ssavice.network.model.UserBookSummaryDTO
import com.ssavice.network.model.UserProfileDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserRetrofitInfoService {
    @GET("/api/user/profile")
    suspend fun getUserProfile(): Response<UserProfileDTO>

    @POST("/api/user/profile")
    suspend fun updateUserProfile(
        @Body body: UpdateUserProfileDTO
    ): Response<UpdateUserProfileResponseDTO>

    @GET("/api/user/book/summary")
    suspend fun getUserParticipationSummary(): Response<UserBookSummaryDTO>

    @POST("/api/user/profile/image")
    suspend fun requestProfileUploadUrl(
        @Body contentType: ContentTypeDTO
    ): Response<PresignedUrlDTO>

    @POST("/api/user/profile/image/confirm")
    suspend fun confirmProfileUpload(
        @Body body: ConfirmImageDTO
    ): Response<Unit>
}
