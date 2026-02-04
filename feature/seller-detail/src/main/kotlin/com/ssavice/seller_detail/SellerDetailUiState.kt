package com.ssavice.seller_detail

import com.ssavice.model.Region
import com.ssavice.model.enums.ServiceState

data class SellerDetailUiState(
    val sellerDetailState: SellerDetailState = SellerDetailState.Initial,
    val sellerInfo: SellerInfoState = SellerInfoState(),
    val serviceItems: List<ServiceItemState> = listOf(),
    val reviewItems: List<ReviewItemState> = listOf(),
    val businessInfo: BusinessInfoState = BusinessInfoState(
        ownerName = "홍길동",
        phoneNumber = "010-1234-5678",
        businessNumber = "123-45-67890"
    )
)

sealed interface SellerDetailState{
    object Initial: SellerDetailState
    object Loading: SellerDetailState
    object Loaded: SellerDetailState
    data class Error(val message: Throwable): SellerDetailState
}

data class SellerInfoState(
    val id: Long = -1L,
    val name: String = "",
    val region: Region = Region(
        0.0,0.0,"", ""
    ),
    val thumbnailUrl: String = "",
    val address: String = "",
    val detailAddress: String = "",
    val description: String = "",
    val detail: String = "",
    val rate: Double = 0.0,
    val rateCount: Int = -1,
    val phoneNumber: String = "",
    val imageUrls: List<String> = listOf()
)

data class ServiceItemState(
    val serviceId: Long,
    val name: String,
    val thumbnailUrl: String,
    val serviceState: ServiceState,
    val category: String,
    val price: String,
    val dayState: String,
    val region: String,
)

data class ReviewItemState(
    val userName: String,
    val comment: String,
    val serviceName: String,
    val createdAt: String,
    val rate: Int
)

data class BusinessInfoState(
    val ownerName: String,
    val phoneNumber: String,
    val businessNumber: String
)

internal fun demoService(id: Int): ServiceItemState = ServiceItemState(
    serviceId = id.toLong(),
    name = "서비스 $id",
    thumbnailUrl = "https://picsum.photos/id/${id*7}/200",
    serviceState = ServiceState.RECRUITING,
    category = "카테고리 $id",
    price = "￦%,d".format("1${id}000".toInt()),
    dayState = "2일 후 마감",
    region = "복현동"
)

internal fun demoReview(id: Int): ReviewItemState = ReviewItemState(
    userName = "유저 $id",
    comment = "리뷰입니다. $id",
    serviceName = "서비스 $id",
    createdAt = "2025-12-12",
    rate = id % 5 + 1
)
