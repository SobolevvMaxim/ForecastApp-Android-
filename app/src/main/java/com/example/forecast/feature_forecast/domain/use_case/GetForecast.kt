package com.example.forecast.feature_forecast.domain.use_case

import com.example.forecast.feature_forecast.data.repository.ForecastRepository
import com.example.forecast.feature_forecast.domain.model.CityWeather

class GetForecast(
    val repository: ForecastRepository,
) {

    suspend operator fun invoke(city: CityWeather): Result<CityWeather> {
        return repository.searchTemp(city)
    }
}