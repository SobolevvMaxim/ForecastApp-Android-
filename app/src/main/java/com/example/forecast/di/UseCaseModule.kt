package com.example.forecast.di

import com.example.forecast.feature_forecast.domain.use_case.DeleteCity
import com.example.forecast.feature_forecast.domain.use_case.GetCityInfo
import com.example.forecast.feature_forecast.domain.use_case.GetForecast
import com.example.forecast.feature_forecast.domain.use_case.UpdateCityForecast
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetCityInfo> {
        GetCityInfo(repository = get())
    }

    factory<DeleteCity> {
        DeleteCity(repository = get())
    }

    factory<GetForecast> {
        GetForecast(repository = get())
    }

    factory<UpdateCityForecast> {
        UpdateCityForecast(repository = get())
    }
}