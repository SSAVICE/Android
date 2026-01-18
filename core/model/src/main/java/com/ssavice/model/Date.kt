package com.ssavice.model

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@JvmInline
value class TimeStamp(
    val timeInMillis: Long,
) {
    init {
        require(timeInMillis >= 0)
    }
}

data class Date(
    val year: Int,
    val month: Int,
    val day: Int,
):Comparable<Date> {
    init {
        require(year > 0)
        require(month > 0)
        require(day > 0)
    }

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
            day = calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    override fun compareTo(other: Date): Int {
        val day = this.year * 10000 + this.month * 100 + this.day
        val otherDay = other.year * 10000 + other.month * 100 + other.day

        if(day > otherDay) return 1
        if(day < otherDay) return -1
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

        fun parse(d: LocalDateTime): Date
        = Date(d.year, d.monthValue, d.dayOfMonth)

        fun now(): Date = parse(LocalDateTime.now())
    }
}

fun main() {
    println(Date.now())
    println(Date.parse(Date.now().toString()))
}
