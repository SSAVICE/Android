package com.ssavice.model.enums

import kotlinx.serialization.Serializable

@Serializable
enum class RoomType(
    val value: String,
) {
    DM("DM"),
    GROUP("GROUP"),
    UNKNOWN("");

    companion object
}

fun RoomType.Companion.getValue(value: String): RoomType {
    return try {
        RoomType.valueOf(value.uppercase())
        } catch (e: IllegalArgumentException) {
        RoomType.UNKNOWN
    }
}
