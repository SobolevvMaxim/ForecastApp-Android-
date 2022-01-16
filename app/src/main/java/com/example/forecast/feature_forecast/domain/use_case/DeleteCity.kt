package com.example.forecast.feature_forecast.domain.use_case

import com.example.forecast.feature_forecast.data.repository.ForecastRepository
import com.example.forecast.feature_forecast.domain.model.CityWeather

class DeleteCity(
    private val repository: ForecastRepository
) {
    // TODO: 16.01.2022 correct result
    suspend operator fun invoke(city: CityWeather): Set<CityWeather> =
        repository.deleteCityInBase(city = city)
}