package com.ssavice.model

import java.util.Calendar
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
    val timeZone: TimeZone = Calendar.getInstance().timeZone,
) {
    init {
        require(year > 0)
        require(month > 0)
        require(day > 0)
    }

    fun toTimeStamp(): TimeStamp {
        val calendar =
            Calendar.getInstance(timeZone).apply {
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

    companion object {
        fun fromTimeStamp(
            timeStamp: TimeStamp,
            timeZone: TimeZone = Calendar.getInstance().timeZone,
        ): Date {
            val calendar =
                Calendar.getInstance(timeZone).apply {
                    timeInMillis = timeStamp.timeInMillis
                }
            return Date(
                year = calendar.get(Calendar.YEAR),
                // Add 1 to convert from Calendar's 0-indexed month to a 1-indexed month.
                month = calendar.get(Calendar.MONTH) + 1,
                day = calendar.get(Calendar.DAY_OF_MONTH),
                timeZone = timeZone,
            )
        }
    }

    fun isAfter(other: Date): Boolean = this.toTimeStamp().timeInMillis > other.toTimeStamp().timeInMillis
}
