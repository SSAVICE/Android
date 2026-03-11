package com.ssavice.common

import kotlin.math.absoluteValue

fun getDeadlineMessageFromTimestamp(
    deadline: Long,
    today: Long,
): String {
    val timeRemaining = deadline - today
    if (timeRemaining < 0) {
        val dayRemaining = timeRemaining.absoluteValue / (1000 * 60 * 60 * 24)
        return "${dayRemaining}일 전 마감"
    } else if (timeRemaining < 1000 * 60 * 60 * 24) {
        val hourRemaining = timeRemaining / (1000 * 60 * 60)

        return "${hourRemaining}시간 후 마감"
    } else {
        val dayRemaining = timeRemaining / (1000 * 60 * 60 * 24)
        return "${dayRemaining}일 후 마감"
    }
}
