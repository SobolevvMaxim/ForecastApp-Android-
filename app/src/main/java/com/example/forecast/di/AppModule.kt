package com.example.forecast.di

import com.example.forecast.feature_forecast.data.repository.ForecastRepository
import com.example.forecast.feature_forecast.domain.repository.IForecastRepository
import com.example.forecast.feature_forecast.presentation.viewmodels.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.viewmodels.MainPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val forecastAppModule = module {
    viewModel {
        CitiesViewModel(
            getCityInfoUseCase = get(),
            getForecastUseCase = get(),
            deleteCityUseCase = get(),
            updateCityUseCase = get(),
            forecastSearchRepos = get()
        )
    }

    viewModel {
        MainPageViewModel(
            getCityInfoUseCase = get(),
            getForecastUseCase = get(),
            forecastSearchRepos = get()
        )
    }

    single<IForecastRepository>(createdAtStart = true) {
        ForecastRepository(
            temperatureService = get(),
            citiesService = get(),
            citiesDao = get()
        )
    }
}

val forecastApp =
    listOf(forecastAppModule, remoteDataSourceModule, localDataSourceModule, useCaseModule)