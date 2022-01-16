package com.example.forecast.feature_forecast.di

import com.example.forecast.feature_forecast.data.local.AppDatabase
import com.example.forecast.feature_forecast.data.local.CitiesDao
import com.example.forecast.feature_forecast.data.remote.services.CitiesService
import com.example.forecast.feature_forecast.data.repository.ForecastRepository
import com.example.forecast.feature_forecast.domain.model.CityWeather
import com.example.forecast.feature_forecast.domain.use_case.DeleteCity
import com.example.forecast.feature_forecast.domain.use_case.GetCityInfo
import com.example.forecast.feature_forecast.domain.use_case.GetForecast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(ActivityRetainedComponent::class)
object CitiesModule {

    @ActivityRetainedScoped
    @Provides
    fun provideCitiesDao(appDatabase: AppDatabase): CitiesDao = appDatabase.citiesDao()

    @ActivityRetainedScoped
    @Provides
    fun provideGetCityInfoUseCase(repository: ForecastRepository): GetCityInfo = GetCityInfo(repository)

    @ActivityRetainedScoped
    @Provides
    fun provideGetForecastUseCase(repository: ForecastRepository): GetForecast = GetForecast(repository)

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteCityUseCase(repository: ForecastRepository): DeleteCity = DeleteCity(repository)

    @ActivityRetainedScoped
    @Provides
    fun provideCitiesService(retrofit: Retrofit): CitiesService =
        retrofit.create(CitiesService::class.java)
}