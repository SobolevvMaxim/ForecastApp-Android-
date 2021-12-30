package com.example.homeworksandroid.di

import com.example.homeworksandroid.database.AppDatabase
import com.example.homeworksandroid.database.CitiesDao
import com.example.homeworksandroid.services.CitiesService
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
    fun provideCitiesService(retrofit: Retrofit): CitiesService =
        retrofit.create(CitiesService::class.java)
}