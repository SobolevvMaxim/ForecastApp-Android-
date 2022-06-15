package com.example.local.di

import com.example.forecast.domain.prefstore.IPrefStore
import com.example.local.prefstore.PrefStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StoreModule {

    @Binds
    abstract fun bindPrefsStore(prefStore: PrefStore): IPrefStore

//    @Binds
//    abstract fun bindSettingsStore(settingsDataStore: SettingsDataStore): ISettingsDataStore
}