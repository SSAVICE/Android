package com.ssavice.model.enums

enum class ServiceState(
    val value: String,
    val visibleAsOption: Boolean = false,
    val cancellable: Boolean = false,
    val reviewable: Boolean = false,
) {
    ALL("전체", visibleAsOption = true),
    RECRUITING("모집 중", visibleAsOption = true, cancellable = true),
    SUCCEEDED("모집 완료", visibleAsOption = true),
    COMPLETED("이용 완료", reviewable = true),
    CANCELED("취소됨", visibleAsOption = true),
    USER_CANCELED("취소함"),
    UNKNOWN("알 수 없음"),
    ;

    companion object
}

fun ServiceState.Companion.mapState(state: String): ServiceState =
    try {
        ServiceState.valueOf(state)
    } catch (e: IllegalArgumentException) {
        ServiceState.UNKNOWN
    }
