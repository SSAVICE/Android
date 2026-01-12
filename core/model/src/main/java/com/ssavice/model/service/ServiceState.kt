package com.ssavice.model.service

enum class ServiceState(
    val value: String,
) {
    APPLYING("모집 중"),
    MATCHED("모집 완료"),
    FAILED("실패"),
    CANCELED("취소됨"),
    COMPLETED("완료됨"),
}
