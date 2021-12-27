package com.example.homeworksandroid.database

import android.util.Log
import androidx.room.TypeConverter
import com.example.homeworksandroid.DailyForecast
import kotlin.collections.ArrayList


class TemperatureConverter {

    @TypeConverter
    fun fromTemperatures(temperatures: ArrayList<DailyForecast>): String {
        val result = StringBuffer()
        temperatures.forEach { daily ->
            result.append("(${daily.temp}, ${daily.description});")
        }
        return result.toString()
//        temperatures.joinToString { it. }
    }

    @TypeConverter
    fun toTemperatures(data: String): ArrayList<DailyForecast> {
        val result: ArrayList<DailyForecast> =
            mutableListOf<DailyForecast>() as ArrayList<DailyForecast>

        var s = data.split(";")

        s = s.filter { it.isNotBlank() }
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