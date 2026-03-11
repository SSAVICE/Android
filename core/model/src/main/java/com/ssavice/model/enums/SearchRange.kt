package com.ssavice.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class SearchRange(
    val index: Int,
    val value: String,
) {
    GUGUN(index = 0, value = ""),
    DONG(index = 1, value = ""),
    KM_1_5(index = 2, value = "1.5km"),
    KM_3(index = 3, value = "3km"),
}
