package com.example.remote.di

import com.example.remote.BuildConfig
import com.example.remote.services.CitiesService
import com.example.remote.services.TemperatureService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun providesNewsRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(okHttpClient)
            .build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                        .let(::addInterceptor)
                }
            }
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val host = originalRequest.url.host

                when {
                    "openweathermap" in host -> originalRequest
                        .newBuilder()
                        .url(
                            originalRequest.url
                                .newBuilder()
                                .addQueryParameter("appid", BuildConfig.API_KEY)
                                .build()
                        )
                    else -> return@addInterceptor chain.proceed(originalRequest)
                }.build().let(chain::proceed)
            }
            .build()
    }

    @Provides
    fun provideCitiesService(retrofit: Retrofit): CitiesService =
        retrofit.create(CitiesService::class.java)

    @Provides
    fun provideTemperatureService(retrofit: Retrofit): TemperatureService =
        retrofit.create(TemperatureService::class.java)
}