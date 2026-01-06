package com.ssavice.data.service

import GetCompanyInfoDTO
import com.ssavice.network.model.AddCompanyDTO
import com.ssavice.network.model.GetCompanySummaryDTO
import com.ssavice.network.model.JWT
import com.ssavice.network.model.LoginDTO
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
    ): Response<JWT>

    @GET("/api/company")
    suspend fun getCompanyInfo(): Response<GetCompanyInfoDTO>

    @GET("/api/company/{id}/summary")
    suspend fun getCompanySummary(
        @Path(value = "id") id: Long,
    ): Response<GetCompanySummaryDTO>
}
