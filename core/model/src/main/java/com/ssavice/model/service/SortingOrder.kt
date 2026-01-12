package com.ssavice.model.service

import kotlinx.serialization.Serializable

@Serializable
enum class SortingOrder(
    val value: Int,
) {
    POPULARITY(0),
    PRICE_ASC(1),
    PRICE_DESC(2),
    DISCOUNT_RATE(3),
    DEADLINE_IMMINENT(4),
}
