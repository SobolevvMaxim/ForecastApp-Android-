package com.example.forecast.domain.data_processing

import java.util.*

enum class AutoUpdateTime {
    ONE_HOUR,
    THREE_HOURS,
    SIX_HOURS;

    override fun toString(): String = when (this) {
        ONE_HOUR -> "one_hour"
        THREE_HOURS -> "three_hours"
        SIX_HOURS -> "six_hours"
    }

    fun getIntTime(): Int = when (this) {
        ONE_HOUR -> 1
        THREE_HOURS -> 3
        SIX_HOURS -> 6
    }

    companion object {
        fun fromString(s: String): AutoUpdateTime {
            return when (s.lowercase(Locale.ROOT)) {
                "one_hour" -> ONE_HOUR
                "three_hours" -> THREE_HOURS
                "six_hours" -> SIX_HOURS
                else -> throw IllegalStateException()
            }
        }
    }
}