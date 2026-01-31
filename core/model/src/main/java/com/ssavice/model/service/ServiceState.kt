package com.ssavice.model.service

enum class ServiceState(
    val value: String,
) {
    ALL("전체"),
    RECRUITING("모집 중"),
    SUCCEEDED("모집 완료"),
    COMPLETED("이용 완료"),
    CANCELED("취소됨"),
    USER_CANCELED("취소함"),
    UNKNOWN("알 수 없음");

    companion object
}

fun ServiceState.Companion.mapState(state: String): ServiceState {
    return try {
        ServiceState.valueOf(state)
    } catch (e: IllegalArgumentException) {
        ServiceState.UNKNOWN
    }
}
