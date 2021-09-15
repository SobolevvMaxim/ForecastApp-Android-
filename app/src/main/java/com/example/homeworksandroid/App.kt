package com.example.homeworksandroid

import android.app.Application
import com.example.homeworksandroid.services.CitiesService
import com.example.homeworksandroid.services.TemperatureService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {

        private val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://api.openweathermap.org")
            .client(getHttpClientWithInterceptor())
            .build()

        val citiesService: CitiesService = retrofit.create(CitiesService::class.java)
        val forecastService: TemperatureService =
            retrofit.create(TemperatureService::class.java)

        private fun getHttpClientWithInterceptor(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }

    }
}