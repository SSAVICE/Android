package com.ssavice.network.retrofit.service

import com.ssavice.network.model.ConfirmImageDTO
import com.ssavice.network.model.ContentTypeDTO
import com.ssavice.network.model.GetAddressDTO
import com.ssavice.network.model.PresignedUrlDTO
import com.ssavice.network.model.RegionPostDTO
import com.ssavice.network.model.service.UserBookDTO
import com.ssavice.network.model.service.WishServiceDTO
import com.ssavice.network.model.user.GetMyIdDTO
import com.ssavice.network.model.user.GetUserInfoDTO
import com.ssavice.network.model.user.UpdateUserProfileDTO
import com.ssavice.network.model.user.UpdateUserProfileResponseDTO
import com.ssavice.network.model.user.UserBookSummaryDTO
import com.ssavice.network.model.user.UserProfileDTO
import com.ssavice.network.model.user.WishListDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserInfoRetrofitService {
    @GET("/api/user/profile")
    suspend fun getUserProfile(): Result<UserProfileDTO>

    @GET("/api/book/user")
    suspend fun getUserBook(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("status") status: String,
    ): Result<UserBookDTO>

    @POST("/api/user/profile")
    suspend fun updateUserProfile(
        @Body body: UpdateUserProfileDTO,
    ): Result<UpdateUserProfileResponseDTO>

    @GET("/api/book/user/summary")
    suspend fun getUserParticipationSummary(): Result<UserBookSummaryDTO>

    @POST("/api/user/profile/image")
    suspend fun requestProfileUploadUrl(
        @Body contentType: ContentTypeDTO,
    ): Result<PresignedUrlDTO>

    @POST("/api/user/profile/image/confirm")
    suspend fun confirmProfileUpload(
        @Body body: ConfirmImageDTO,
    ): Result<Unit>

    @PATCH("/api/user/address")
    suspend fun updateUserAddress(
        @Body body: RegionPostDTO,
    ): Result<Unit>

    @GET("/api/user/address")
    suspend fun getUserAddress(): Result<GetAddressDTO>

    @POST("/api/user/wish/{id}")
    suspend fun wishService(
        @Path(value = "id") id: Long,
        @Body body: WishServiceDTO,
    ): Result<Unit>

    @GET("/api/user/wish")
    suspend fun getWish(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Result<WishListDTO>

    @GET("/api/chat-members")
    suspend fun getUserInfoSummary(
        @Query("ids")userIds: List<Long>,
    ): Result<GetUserInfoDTO>

    @GET("/api/auth/id")
    suspend fun getMyUserId(): Result<GetMyIdDTO>

    @DELETE("/api/account")
    suspend fun unregisterAccount(): Result<Unit>
}
