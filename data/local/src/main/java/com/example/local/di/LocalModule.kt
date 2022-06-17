package com.example.local.di

import android.content.Context
import androidx.room.Room
import com.example.local.AppDatabase
import com.example.local.CityWeatherDao
import com.example.local.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
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

    @DefaultChosenID
    @Provides
    fun provideDefaultChosenCityID(@ApplicationContext context: Context) =
        context.getString(R.string.default_chosen_id)
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultChosenID