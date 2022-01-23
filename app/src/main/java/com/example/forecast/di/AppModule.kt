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
    fun provideGetCityTag(@ApplicationContext context: Context): String {
        return context.getString(R.string.get_city_extra)
    }

    @Singleton
    @Provides
    fun provideDateFormat() = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    @MainCoroutineDispatcher
    @Provides
    fun mainCoroutineDispatcherProvider(): CoroutineDispatcher = Dispatchers.Main

    @IOCoroutineDispatcher
    @Provides
    fun ioCoroutineDispatcherProvider(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainCoroutineDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IOCoroutineDispatcher