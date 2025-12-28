package com.ssavice.data.service

import com.ssavice.network.model.AddCompanyDTO
import com.ssavice.network.model.JWT
import com.ssavice.network.model.LoginDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CompanyRetrofitService {
    @POST("/api/company")
    suspend fun registerSeller(
        @Body body: AddCompanyDTO,
    ): Response<Unit>

    @POST("/api/company/login")
    suspend fun loginSeller(
        @Body body: LoginDTO,
    ): Response<JWT>
}
