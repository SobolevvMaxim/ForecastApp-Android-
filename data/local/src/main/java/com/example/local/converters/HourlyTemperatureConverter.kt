package com.example.local.converters

import androidx.room.TypeConverter
import com.example.forecast.domain.model.Hourly

class HourlyTemperatureConverter {

    @TypeConverter
    fun fromTemperatures(temperatures: ArrayList<Hourly>): String =
        temperatures.joinToString(separator = "", transform = { daily ->
            "(${daily.temp}, ${daily.description});"
        })

    @TypeConverter
    fun toTemperatures(data: String): ArrayList<Hourly> {
        val s = data.split(";").filter { it.isNotBlank() }

        return ArrayList(s.map {
            val split = it.split(", ")
            val number = split[0].substring(1)
            val description = split[1].subSequence(0, split[1].length - 1)
            Hourly(temp = number.toDouble(), description = description.toString())
        })
    }
}