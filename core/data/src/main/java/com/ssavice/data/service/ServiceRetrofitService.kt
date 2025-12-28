package com.ssavice.data.service

import com.ssavice.network.model.AddServiceDTO
import com.ssavice.network.model.AddServiceResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceRetrofitService {
    @POST("/api/service")
    suspend fun postService(
        @Body body: AddServiceDTO,
    ): Response<AddServiceResponseDTO>
}
