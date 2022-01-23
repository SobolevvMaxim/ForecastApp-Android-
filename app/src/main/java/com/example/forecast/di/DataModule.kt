package com.example.forecast.di

import android.content.Context
import androidx.room.Room
import com.example.forecast.data.local.AppDatabase
import com.example.forecast.data.local.CityWeatherDao
import com.example.forecast.data.remote.services.CitiesService
import com.example.forecast.data.remote.services.TemperatureService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun providesNewsRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://api.openweathermap.org")
            .client(okHttpClient)
            .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    fun provideCitiesDao(appDatabase: AppDatabase): CityWeatherDao = appDatabase.citiesDao()

    @Provides
    fun provideCitiesService(retrofit: Retrofit): CitiesService =
        retrofit.create(CitiesService::class.java)

    @Provides
    fun provideTemperatureService(retrofit: Retrofit): TemperatureService =
        retrofit.create(TemperatureService::class.java)
}