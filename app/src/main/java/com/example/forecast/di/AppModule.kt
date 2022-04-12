package com.example.forecast.di

import android.content.Context
import com.example.forecast.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @PreferenceTag
    fun provideGetCityTag(@ApplicationContext context: Context): String {
        return context.getString(R.string.get_city_extra)
    }

    @DateFormat
    @Singleton
    @Provides
    fun provideDateFormat() = SimpleDateFormat("dd.MM.yyyy 'at' HH:mm", Locale("ru"))

    @TimeFormat
    @Singleton
    @Provides
    fun provideTimeFormat() = SimpleDateFormat("HH:mm", Locale("ru"))

    @Provides
    fun ioCoroutineDispatcherProvider(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DateFormat

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TimeFormat

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PreferenceTag