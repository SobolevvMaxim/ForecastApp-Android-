package com.example.homeworksandroid.database

import androidx.room.TypeConverter
import kotlin.collections.ArrayList


class TemperatureConverter {

    @TypeConverter
    fun fromTemperatures(temperatures: ArrayList<Pair<Int, String>>): String =
        temperatures.joinToString(separator = ";")

    @TypeConverter
    fun toTemperatures(data: String): ArrayList<Pair<Int, String>> {
        val result: ArrayList<Pair<Int, String>> =
            mutableListOf<Pair<Int, String>>() as ArrayList<Pair<Int, String>>

        val s = data.split(";")

        s.forEach {
            val split = it.split(", ")
            val number = split[0].substring(1)
            val description = split[1].subSequence(0, split[1].length - 1)
            result.add(Pair(number.toInt(), description.toString()))
        }

        return result
    }
}