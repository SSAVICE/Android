package com.ssavice.data.service

import GetCompanyInfoDTO
import com.ssavice.network.model.company.AddCompanyDTO
import com.ssavice.network.model.company.CompanyBookDTO
import com.ssavice.network.model.company.CompanyBookSummaryDTO
import com.ssavice.network.model.company.CompanyDetailDTO
import com.ssavice.network.model.ConfirmImageDTO
import com.ssavice.network.model.ContentTypeDTO
import com.ssavice.network.model.GetAddressDTO
import com.ssavice.network.model.review.GetCompanyReviewDTO
import com.ssavice.network.model.company.GetCompanySummaryDTO
import com.ssavice.network.model.JwtDTO
import com.ssavice.network.model.LoginDTO
import com.ssavice.network.model.PresignedUrlDTO
import com.ssavice.network.model.company.UpdateCompanyProfileDTO
import com.ssavice.network.model.company.ValidateBusinessDTO
import com.ssavice.network.model.company.ValidateBusinessResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CompanyRetrofitService {
    @POST("/api/company")
    suspend fun registerSeller(
        @Body body: AddCompanyDTO,
    ): Response<Unit>

    @POST("/api/company/login")
    suspend fun loginSeller(
        @Body body: LoginDTO,
    ): Response<JwtDTO>

    @GET("/api/company")
    suspend fun getCompanyInfo(): Response<GetCompanyInfoDTO>

    @GET("/api/company/{id}/summary")
    suspend fun getCompanySummary(
        @Path(value = "id") id: Long,
    ): Response<GetCompanySummaryDTO>

    @POST("/api/company/validate")
    suspend fun validateBusinessInfo(
        @Body body: ValidateBusinessDTO,
    ): Response<ValidateBusinessResponseDTO>

    @GET("/api/service/company/summary")
    suspend fun getCompanyParticipationSummary(): Response<CompanyBookSummaryDTO>

    @GET("/api/company/address")
    suspend fun getCompanyAddress(): Response<GetAddressDTO>

    @GET("/api/service/company/my")
    suspend fun getCompanyBook(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("status") status: String,
    ): Response<CompanyBookDTO>

    @POST("/api/company/image")
    suspend fun requestProfileUploadUrl(
        @Body contentType: ContentTypeDTO,
    ): Response<PresignedUrlDTO>

    @POST("/api/company/image/confirm")
    suspend fun confirmProfileUpload(
        @Body body: ConfirmImageDTO,
    ): Response<Unit>

    @PUT("/api/company")
    suspend fun putCompanyProfile(
        @Body body: UpdateCompanyProfileDTO,
    ): Response<Unit>

    @GET("/api/company/{id}")
    suspend fun getCompanyDetail(
        @Path(value = "id") id: Long,
    ): Response<CompanyDetailDTO>

    @GET("/api/review/company/{id}")
    suspend fun getCompanyReview(
        @Path(value = "id") id: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<GetCompanyReviewDTO>
}
