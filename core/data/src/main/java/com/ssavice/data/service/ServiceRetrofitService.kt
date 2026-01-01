package com.ssavice.data.service

import com.ssavice.network.model.AddServiceDTO
import com.ssavice.network.model.AddServiceResponseDTO
import com.ssavice.network.model.SearchServiceResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ServiceRetrofitService {
    @POST("/api/service")
    suspend fun postService(
        @Body body: AddServiceDTO,
    ): Response<AddServiceResponseDTO>

    @GET("/api/service/search")
    suspend fun searchService(
        @QueryMap options: Map<String, String>
    ): Response<SearchServiceResponseDTO>
}
