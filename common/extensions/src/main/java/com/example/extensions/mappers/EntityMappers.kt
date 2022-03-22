package com.example.extensions.mappers

import com.example.forecast.domain.model.CityWeather
import com.example.local.entities.CityWeatherEntity

object EntityMappers {
    fun CityWeatherEntity.toCityWeather(): CityWeather {
        return CityWeather(
            id,
            name,
            country,
            lat,
            lon,
            dailyTemperatures,
            hourlyTemperatures,
            sunrise,
            sunset,
            feels_like,
            humidity,
            uvi,
            forecastDate
        )
    }
}