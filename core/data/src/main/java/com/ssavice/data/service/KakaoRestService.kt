package com.ssavice.data.service

import com.ssavice.network.model.KakaoGetCoordinateDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface KakaoRestService {
    @GET("/v2/local/search/address.{format}")
    suspend fun getCoordinateFromAddress(
        @Header("Authorization") authorization: String,
        @Path("format") format: String,
        @Query("query") query: String,
        @Query("analyze_type") analyzeType: String = "exact",
    ): Response<KakaoGetCoordinateDTO>
}
