package com.ssavice.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class SortingOrder(
    val index: Int,
    val value: String,
) {
    LATEST(index = 0, value = "최신 순"),
    PRICE_ASC(index = 1, value = "가격 낮은 순"),
    PRICE_DESC(index = 2, value = "가격 높은 순"),
    DISCOUNT_RATE(index = 3, value = "할인율 순"),
    DISTANCE(index = 4, "거리 순"),
}
