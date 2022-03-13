package com.example.remote.dto

import android.util.Log
import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Daily
import com.example.forecast.domain.model.Hourly
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

data class TemperaturesDto(
    @SerializedName("hourly") val hourlyTemp: List<HourlyTemp>,
    @SerializedName("daily") val dailyTemp: List<DailyTemp>,
    @SerializedName("current") val current: Current,
) {
    fun toCityWeather(city: City): CityWeather {
        val daily = ArrayList(
            dailyTemp.map {
                Daily(temp = it.temp.day.roundToInt() - 273, description = it.weather[0].main)
            })
        val hourly = ArrayList(
            hourlyTemp.map {
                Hourly(temp = it.temp.roundToInt() - 273, description = it.weather[0].main)
            }
        )
        val cityWeather = CityWeather(
            id = city.id,
            name = city.name,
            country = city.country,
            lat = city.coordinates.lat,
            lon = city.coordinates.lon,
            dailyTemperatures = daily,
            hourlyTemperatures = hourly,
            sunrise = current.sunrise,
            sunset = current.sunset,
            feels_like = current.feels_like,
            humidity = current.humidity,
            uvi = current.uvi,
            forecastDate = SimpleDateFormat(
                "dd.MM.yyyy 'at' HH:mm",
                Locale.getDefault()
            ).format(Calendar.getInstance().time),
        )
        Log.d("HOUR", "toCityWeather: $hourlyTemp")
        return cityWeather
    }
}

data class Current(
    val sunrise: String,
    val sunset: String,
    val feels_like: Double,
    val humidity: Int,
    val uvi: Double
)

data class DailyTemp(
    val temp: Temp,
    val weather: List<WeatherData>
)

data class HourlyTemp(
    val temp: Double,
    val weather: List<WeatherData>
)

data class Temp(
    val day: Double,
)

data class WeatherData(
    val id: String?,
    val main: String,
    val description: String,
)