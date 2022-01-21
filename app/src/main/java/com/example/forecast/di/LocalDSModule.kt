package com.example.forecast.di

import androidx.room.Room
import com.example.forecast.APP_DATABASE
import com.example.forecast.feature_forecast.data.local.AppDatabase
import com.example.forecast.feature_forecast.data.local.CitiesDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.*

val localDataSourceModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            APP_DATABASE
        ).build()
    }

    factory<SimpleDateFormat> {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    }

    factory<CitiesDao> {
        createCitiesDao(get())
    }
}

fun createCitiesDao(appDatabase: AppDatabase) = appDatabase.citiesDao()