package com.ssavice.common

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeParseException

object DomainFormatter {
    fun formatPrice(price: Int): String = "₩%,d".format(price)

    fun formatPrice(price: Long): String = "₩%,d".format(price)

    fun formatTimeToMilliseconds(time: String): Long {
        return try {
            // ISO_OFFSET_DATE_TIME은 "2024-03-27T10:15:30+09:00" 형식을 지원합니다.
            // 서버 응답 형식이 "2024-03-27T10:15:30" (Z가 없는 Local)일 경우 LocalDateTime을 써야 할 수도 있습니다.
            OffsetDateTime.parse(time).toInstant().toEpochMilli()
        } catch (e: DateTimeParseException) {
            LocalDateTime.parse(time.split('+')[0].split('Z')[0]).toInstant(ZoneOffset.UTC).toEpochMilli()
        } catch (e: Exception) {
            0
        }
    }
}
