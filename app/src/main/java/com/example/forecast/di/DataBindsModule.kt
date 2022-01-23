package com.example.forecast.di

import com.example.forecast.data.repository.ForecastRepository
import com.example.forecast.domain.repository.IForecastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class DataBindsModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindForecastRepository(forecastRepository: ForecastRepository): IForecastRepository
}