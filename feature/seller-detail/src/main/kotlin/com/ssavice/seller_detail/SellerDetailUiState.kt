package com.ssavice.seller_detail

import com.ssavice.model.enums.ServiceState

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
