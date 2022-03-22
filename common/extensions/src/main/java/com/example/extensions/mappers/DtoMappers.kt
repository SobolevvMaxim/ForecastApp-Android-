package com.example.extensions.mappers

import android.util.Log
import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Daily
import com.example.forecast.domain.model.Hourly
import com.example.remote.dto.CityDto
import com.example.remote.dto.TemperaturesDto
import java.text.SimpleDateFormat
import java.util.*

object DtoMappers {
    fun CityDto.toCity(name: String) = City(
        coordinates = coordinates,
        id = id,
        name = name,
        country = sys.country
    )

    fun TemperaturesDto.toCityWeather(city: City): CityWeather {
        val daily = ArrayList(
            dailyTemp.map {
                Daily(temp = it.temp.day, description = it.weather[0].main)
            })
        val hourly = ArrayList(
            hourlyTemp.map {
                Hourly(temp = it.temp, description = it.weather[0].main)
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