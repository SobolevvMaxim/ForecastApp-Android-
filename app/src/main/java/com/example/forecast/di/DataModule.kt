package com.example.forecast.di

import android.content.Context
import androidx.room.Room
import com.example.db.AppDatabase
import com.example.db.CityWeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val APP_DATABASE = "APP_DATABASE"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_DATABASE
        ).build()

    @Provides
    fun provideCitiesDao(appDatabase: AppDatabase): CityWeatherDao = appDatabase.citiesDao()


}