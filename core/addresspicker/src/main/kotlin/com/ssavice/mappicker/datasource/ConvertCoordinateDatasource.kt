package com.ssavice.mappicker.datasource

import com.ssavice.core.mappicker.BuildConfig
import com.ssavice.data.service.KakaoRestService
import com.ssavice.mappicker.model.CoordinateConvertResult
import javax.inject.Inject

class ConvertCoordinateDatasource
    @Inject
    constructor(
        private val kakaoRestService: KakaoRestService,
    ) {
        suspend fun convertCoordinate(address: String): Result<CoordinateConvertResult> {
            return kakaoRestService.getCoordinateFromAddress(
                authorization = "KakaoAK ${BuildConfig.KAKAO_REST_KEY}",
                format = "json",
                query = address,
            ).map {
                if (it.documents.isEmpty()) {
                    return Result.failure(IllegalArgumentException("Can't find address"))
                } else {
                    CoordinateConvertResult(
                        x = it.documents[0].x,
                        y = it.documents[0].y,
                    )
                }
            }
        }
    }
