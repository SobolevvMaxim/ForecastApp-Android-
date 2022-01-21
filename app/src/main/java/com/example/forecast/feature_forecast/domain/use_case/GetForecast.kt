package com.example.forecast.feature_forecast.domain.use_case

import com.example.forecast.feature_forecast.data.repository.ForecastRepository
import com.example.forecast.feature_forecast.domain.model.City
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.domain.repository.IForecastRepository

class GetForecast(
    val repository: IForecastRepository,
) {

    suspend operator fun invoke(city: City): Result<CityWeather> {
        return repository.searchForecast(city)
    }
}