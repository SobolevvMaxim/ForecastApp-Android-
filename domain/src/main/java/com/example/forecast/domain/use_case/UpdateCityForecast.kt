package com.example.forecast.domain.use_case

import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.repository.IForecastRepository

class UpdateCityForecast(
    val repository: IForecastRepository,
) {
    suspend operator fun invoke(city: CityWeather): Set<CityWeather> {
        return repository.updateCityInBase(
            city = city
        )
    }
}