package com.example.forecast.di

import com.example.forecast.feature_forecast.presentation.prefstore.IPrefStore
import com.example.forecast.feature_forecast.presentation.prefstore.PrefStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StoreModule {

    @Binds
    abstract fun bindPrefsStore(prefStore: PrefStore): IPrefStore
}