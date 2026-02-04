package com.ssavice.common

fun getDeadlineMessageFromTimestamp(deadline: Long, today: Long): String {
    val timeRemaining = deadline - today
    if (timeRemaining < 1000 * 60 * 60 * 24) {
        val hourRemaining = timeRemaining / (1000 * 60 * 60)

        return "${hourRemaining}시간 후 마감"
    } else {
        val dayRemaining = timeRemaining / (1000 * 60 * 60 * 24)
        return "${dayRemaining}일 후 마감"
    }
}
