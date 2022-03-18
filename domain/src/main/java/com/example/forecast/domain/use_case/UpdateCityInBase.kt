package com.example.forecast.domain.use_case

import com.example.forecast.domain.model.CityWeather
import com.example.forecast.domain.repository.IForecastRepository

class UpdateCityInBase(
    val repository: IForecastRepository,
) {
    suspend operator fun invoke(city: CityWeather) {
        return repository.updateCityInBase(
            city = city
        )
    }
}