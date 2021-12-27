package com.example.homeworksandroid.database

import android.util.Log
import androidx.room.TypeConverter
import com.example.homeworksandroid.DailyForecast
import com.example.homeworksandroid.activities.P_LOG
import okhttp3.internal.toImmutableList
import kotlin.collections.ArrayList


class TemperatureConverter {

    @TypeConverter
    fun fromTemperatures(temperatures: ArrayList<DailyForecast>): String =
        temperatures.joinToString(separator = "", transform = { daily ->
            "(${daily.temp}, ${daily.description});"
        })

    @TypeConverter
    fun toTemperatures(data: String): ArrayList<DailyForecast> {
        val result = mutableListOf<DailyForecast>() as ArrayList<DailyForecast>

        val s = data.split(";").filter { it.isNotBlank() }

        Log.d("ParcebleTest", "toTemperatures $s")

        s.forEach {
            val split = it.split(", ")
            val number = split[0].substring(1)
            val description = split[1].subSequence(0, split[1].length - 1)
            result.add(DailyForecast(temp = number.toInt(), description = description.toString()))
        }

        return result
    }
}