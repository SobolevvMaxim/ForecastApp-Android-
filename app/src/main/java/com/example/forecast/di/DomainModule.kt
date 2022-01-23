package com.example.forecast.di

import com.example.forecast.domain.repository.IForecastRepository
import com.example.forecast.domain.use_case.DeleteCity
import com.example.forecast.domain.use_case.GetCityInfo
import com.example.forecast.domain.use_case.GetForecast
import com.example.forecast.domain.use_case.UpdateCityForecast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @ViewModelScoped
    @Provides
    fun provideGetCityInfoUseCase(repository: IForecastRepository) = GetCityInfo(repository)

    @ViewModelScoped
    @Provides
    fun provideGetForecastUseCase(repository: IForecastRepository) = GetForecast(repository)

    @ViewModelScoped
    @Provides
    fun provideDeleteCityUseCase(repository: IForecastRepository) = DeleteCity(repository)

    @ViewModelScoped
    @Provides
    fun provideUpdateCityUseCase(repository: IForecastRepository) = UpdateCityForecast(repository)
}