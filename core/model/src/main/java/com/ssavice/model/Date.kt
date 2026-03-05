package com.ssavice.model

import com.ssavice.model.Date.Companion.DEFAULT_TIME_ZONE
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Serializable
@JvmInline
value class TimeStamp(
    val timeInMillis: Long,
) {
    init {
        require(timeInMillis >= 0)
    }
}

data class DateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
) {
    fun toTimeStamp(): TimeStamp {
        val calendar =
            Calendar.getInstance(DEFAULT_TIME_ZONE).apply {
                set(Calendar.YEAR, year)
                // Calendar months are 0-indexed (January is 0), so we subtract 1.
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, day)
                // Set time to the beginning of the day to avoid time-of-day discrepancies.
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.MILLISECOND, 0)
            }

        return TimeStamp(calendar.timeInMillis)
    }

    fun timeToSimpleString(): String {
        val localDateTime = LocalDateTime.of(year, month, day, hour, minute)
        return localDateTime.format(chatTimeFormatter)
    }

    fun absoluteDateSimpleString(): String {
        val now = LocalDateTime.now(DEFAULT_TIME_ZONE.toZoneId())

        return if (now.year == this.year) {
            // 올해인 경우: M월 d일
            this.toTimeStamp().let {
                val ldt = LocalDateTime.of(year, month, day, hour, minute)
                ldt.format(absoluteMonthDayFormatter)
            }
        } else {
            // 올해가 아닌 경우: yyyy년 M월 d일
            val ldt = LocalDateTime.of(year, month, day, hour, minute)
            ldt.format(absoluteYearMonthDayFormatter)
        }
    }

    fun dateToSimpleString(): String {
        val now = LocalDateTime.now(DEFAULT_TIME_ZONE.toZoneId())
        val target = LocalDateTime.of(year, month, day, hour, minute)

        val nowDay = now.toLocalDate()
        val targetDay = target.toLocalDate()

        return when {
            // 1. 오늘인 경우 (이미 만든 시:분 포맷 사용)
            nowDay == targetDay -> "오늘"

            // 2. 어제인 경우
            nowDay.minusDays(1) == targetDay -> "어제"

            // 3. 같은 연도인 경우 (3월 2일)
            nowDay.year == targetDay.year -> target.format(monthDayFormatter)

            // 4. 연도가 다른 경우 (2024. 3. 2.)
            else -> target.format(yearMonthDayFormatter)
        }
    }

    companion object {
        fun fromTimeStamp(timeStamp: Long): DateTime {
            val instant = java.time.Instant.ofEpochMilli(timeStamp)
            // KST(Asia/Seoul) 또는 설정된 DEFAULT_TIME_ZONE에 맞춰 변환
            val ldt = LocalDateTime.ofInstant(instant, DEFAULT_TIME_ZONE.toZoneId())

            return DateTime(
                year = ldt.year,
                month = ldt.monthValue,
                day = ldt.dayOfMonth,
                hour = ldt.hour,
                minute = ldt.minute,
            )
        }

        @Suppress("ConstantLocale")
        val chatTimeFormatter: java.time.format.DateTimeFormatter =
            java.time.format.DateTimeFormatter
                .ofPattern("a h:mm", Locale.getDefault())

        @Suppress("ConstantLocale")
        val monthDayFormatter: java.time.format.DateTimeFormatter =
            java.time.format.DateTimeFormatter
                .ofPattern("M월 d일", Locale.getDefault())

        @Suppress("ConstantLocale")
        val yearMonthDayFormatter: java.time.format.DateTimeFormatter =
            java.time.format.DateTimeFormatter
                .ofPattern("yyyy. M. d.", Locale.getDefault())

        @Suppress("ConstantLocale")
        val absoluteMonthDayFormatter: java.time.format.DateTimeFormatter =
            java.time.format.DateTimeFormatter
                .ofPattern("M월 d일", Locale.getDefault())

        @Suppress("ConstantLocale")
        val absoluteYearMonthDayFormatter: java.time.format.DateTimeFormatter =
            java.time.format.DateTimeFormatter
                .ofPattern("yyyy년 M월 d일", Locale.getDefault())
    }
}

data class Date(
    val year: Int,
    val month: Int,
    val day: Int,
) : Comparable<Date> {
    fun toTimeStamp(): TimeStamp {
        val calendar =
            Calendar.getInstance(DEFAULT_TIME_ZONE).apply {
                set(Calendar.YEAR, year)
                // Calendar months are 0-indexed (January is 0), so we subtract 1.
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, day)
                // Set time to the beginning of the day to avoid time-of-day discrepancies.
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

        return TimeStamp(calendar.timeInMillis)
    }

    override fun toString(): String {
        val timeStamp = this.toTimeStamp().timeInMillis
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREAN)
        sdf.timeZone = DEFAULT_TIME_ZONE
        return sdf.format(java.util.Date(timeStamp))
    }

    fun toSimpleString(): String {
        val timeStamp = this.toTimeStamp().timeInMillis
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.timeZone = DEFAULT_TIME_ZONE
        return sdf.format(java.util.Date(timeStamp))
    }

    fun addDay(daysToAdd: Int): Date {
        val calendar =
            Calendar.getInstance(DEFAULT_TIME_ZONE).apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month - 1)
                set(Calendar.DAY_OF_MONTH, day)
            }

        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)

        return Date(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1,
            day = calendar.get(Calendar.DAY_OF_MONTH),
        )
    }

    override fun compareTo(other: Date): Int {
        val day = this.year * 10000 + this.month * 100 + this.day
        val otherDay = other.year * 10000 + other.month * 100 + other.day

        if (day > otherDay) return 1
        if (day < otherDay) return -1
        return 0
    }

    companion object {
        val DEFAULT_TIME_ZONE: TimeZone = TimeZone.getTimeZone("KST")

        fun parse(
            timeStamp: TimeStamp,
            timeZone: TimeZone = Calendar.getInstance().timeZone,
        ): Date {
            val calendar =
                Calendar.getInstance(timeZone).apply {
                    timeInMillis = timeStamp.timeInMillis
                }
            calendar.timeZone = DEFAULT_TIME_ZONE
            return Date(
                year = calendar.get(Calendar.YEAR),
                // Add 1 to convert from Calendar's 0-indexed month to a 1-indexed month.
                month = calendar.get(Calendar.MONTH) + 1,
                day = calendar.get(Calendar.DAY_OF_MONTH),
            )
        }

        fun parse(s: String): Date {
            val timeParsed =
                try {
                    LocalDateTime.parse(s.split('+')[0].split('Z')[0])
                } catch (e: Exception) {
                    println("Error parsing date: ${e.stackTraceToString()}")
                    LocalDateTime.MIN
                }
            return parse(timeParsed)
        }

        fun parse(d: LocalDateTime): Date = Date(d.year, d.monthValue, d.dayOfMonth)

        fun now(): Date = parse(LocalDateTime.now())
    }
}

fun main() {
    println(Date.now())
    println(Date.parse(Date.now().toString()))
}
