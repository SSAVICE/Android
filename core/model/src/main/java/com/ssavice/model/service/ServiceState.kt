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
}
