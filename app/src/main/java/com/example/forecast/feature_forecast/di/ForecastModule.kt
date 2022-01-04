package com.example.forecast.feature_forecast.di

import com.example.forecast.feature_forecast.data.remote.services.TemperatureService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object ForecastModule {

    @ActivityRetainedScoped
    @Provides
    fun provideTemperatureService(retrofit: Retrofit): TemperatureService =
        retrofit.create(TemperatureService::class.java)
}