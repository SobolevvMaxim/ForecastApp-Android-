package com.example.forecast.di

import com.example.forecast.feature_forecast.data.remote.services.CitiesService
import com.example.forecast.feature_forecast.data.remote.services.TemperatureService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val remoteDataSourceModule = module {
    single {
        createOkHttpClient()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://api.openweathermap.org")
            .client(get())
            .build()
    }

    single<CitiesService> {
        createWebService<CitiesService>(get(), get())
    }

    single<TemperatureService> {
        createWebService<TemperatureService>(get(), get())
    }
}

fun createOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, retrofit: Retrofit): T =
   retrofit.create(T::class.java)
