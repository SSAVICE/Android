package com.ssavice.data.service

import GetCompanyInfoDTO
import com.ssavice.network.model.AddCompanyDTO
import com.ssavice.network.model.GetCompanySummaryDTO
import com.ssavice.network.model.JwtDTO
import com.ssavice.network.model.LoginDTO
import com.ssavice.network.model.ValidateBusinessDTO
import com.ssavice.network.model.ValidateBusinessResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
}
