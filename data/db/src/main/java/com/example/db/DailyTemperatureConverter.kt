package com.example.db

import androidx.room.TypeConverter
import com.example.forecast.domain.model.Daily


class DailyTemperatureConverter {

    @TypeConverter
    fun fromTemperatures(temperatures: ArrayList<Daily>): String =
        temperatures.joinToString(separator = "", transform = { daily ->
            "(${daily.temp}, ${daily.description});"
        })

    @TypeConverter
    fun toTemperatures(data: String): ArrayList<Daily> {
        val s = data.split(";").filter { it.isNotBlank() }

        return ArrayList(s.map {
            val split = it.split(", ")
            val number = split[0].substring(1)
            val description = split[1].subSequence(0, split[1].length - 1)
            Daily(temp = number.toInt(), description = description.toString())
        })
    }
}