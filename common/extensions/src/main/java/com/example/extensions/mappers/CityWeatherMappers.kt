package com.example.extensions.mappers

import com.example.forecast.domain.model.City
import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.model.Coordinates
import com.example.local.entities.CityWeatherEntity

object CityWeatherMappers {

    fun CityWeather.toCityWeatherEntity() = CityWeatherEntity(
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

    fun CityWeather.toCity() = City(
        coordinates = Coordinates(
            lat = lat,
            lon = lon
        ),
        id = id,
        name = name,
        country = country
    )
}