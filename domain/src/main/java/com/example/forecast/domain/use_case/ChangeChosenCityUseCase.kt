package com.example.forecast.domain.use_case

import com.example.forecast.domain.repository.IForecastRepository

class ChangeChosenCityUseCase(
    val repository: IForecastRepository
) {

    suspend operator fun invoke(lastChosenIndex: Int, newChosenIndex: Int) =
        repository.changeChosenCityByName(lastChosenIndex, newChosenIndex)
}