package com.example.homeworksandroid

data class CityWeather(
    val name: String,
    val id: String,
    val country: String,
    val lat: String,
    val lon: String,
    var temperatures: ArrayList<Pair<Int, String>> = mutableListOf<Pair<Int, String>>() as ArrayList<Pair<Int, String>>,
    var chosen: Boolean = false
    )