package com.ssavice.model

/**
 * 주소 및 위치 정보를 포함하는 데이터 모델입니다.
 * 주로 관련 정보를 생성하거나 수정하는 **POST 요청** 시에 사용되는 모델입니다.
 *
 * @property latitude 위도
 * @property longitude 경도
 * @property address 기본 주소 (예: 대구 달서구 데모로 321)
 * @property detailAddress 상세 주소 (예: 데모아파트 101동 1005호)
 * @property postCode 우편번호 (5자리)
 * @property regionCode 지역 코드 (행정동 코드 등)
 */
data class RegionInfo(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val detailAddress: String,
    val postCode: String,
    val regionCode: String,
) {
    companion object {
        val demo =
            RegionInfo(
                latitude = 10.1,
                longitude = 11.2,
                address = "대구 달서구 데모로 321",
                detailAddress = "데모아파트 101동 1005호",
                postCode = "12345",
                regionCode = "123456789",
            )
    }
}

/**
 * 특정 지역의 위치 및 행정 구역 정보를 나타내는 모델입니다.
 * 주로 서버로부터 데이터를 조회하는 **GET 요청** 시 결과 도메인으로 쓰입니다.
 *
 * @property latitude 위도
 * @property longitude 경도
 * @property region1 행정 구역 중 '구/군' 단위 (예: 달서구, 수성구)
 * @property region2 행정 구역 중 '읍/면/동' 단위 (예: 상인동, 범어동)
 */
data class Region(
    val latitude: Double,
    val longitude: Double,
    val region1: String,
    val region2: String,
)

data class RegionDetail(
    val regionInfo: RegionInfo,
    val region1: String,
    val region2: String
)
