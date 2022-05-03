package com.example.extensions.mappers

import android.util.Log
import com.example.forecast.domain.model.CityToSearch
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Daily
import com.example.forecast.domain.model.Hourly
import com.example.remote.dto.CityDto
import com.example.remote.dto.TemperaturesDto
import java.text.SimpleDateFormat
import java.util.*

object DtoMappers {
    fun CityDto.toCity(name: String) = CityToSearch(
        coordinates = coordinates,
        searchName = name
    )

    fun TemperaturesDto.toCityWeather(cityToSearch: CityToSearch): CityWeather {
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
            id = UUID.nameUUIDFromBytes(cityToSearch.searchName.encodeToByteArray()).toString(),
            name = cityToSearch.searchName,
            country = timezone.substring(0, 2),
            lat = cityToSearch.coordinates!!.lat,
            lon = cityToSearch.coordinates!!.lon,
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