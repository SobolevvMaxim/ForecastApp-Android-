package com.example.forecast.di

import com.example.forecast.data.local.AppDatabase
import com.example.forecast.data.local.CitiesDao
import com.example.forecast.data.remote.services.CitiesService
import com.example.forecast.domain.repository.IForecastRepository
import com.example.forecast.domain.use_case.DeleteCity
import com.example.forecast.domain.use_case.GetCityInfo
import com.example.forecast.domain.use_case.GetForecast
import com.example.forecast.domain.use_case.UpdateCityForecast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit


@Module
@InstallIn(ActivityRetainedComponent::class)
object CitiesModule {

    @ActivityRetainedScoped
    @Provides
    fun provideCitiesDao(appDatabase: AppDatabase): CitiesDao = appDatabase.citiesDao()

    @ActivityRetainedScoped
    @Provides
    fun provideGetCityInfoUseCase(repository: IForecastRepository): GetCityInfo = GetCityInfo(repository)

    @ActivityRetainedScoped
    @Provides
    fun provideGetForecastUseCase(repository: IForecastRepository): GetForecast = GetForecast(repository)

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteCityUseCase(repository: IForecastRepository): DeleteCity = DeleteCity(repository)

    @ActivityRetainedScoped
    @Provides
    fun provideUpdateCityUseCase(repository: IForecastRepository): UpdateCityForecast = UpdateCityForecast(repository)

    @ActivityRetainedScoped
    @Provides
    fun provideCitiesService(retrofit: Retrofit): CitiesService =
        retrofit.create(CitiesService::class.java)
}