package com.example.forecast.ui.settings.di

import com.example.forecast.domain.prefstore.ISettingsPreferences
import com.example.forecast.ui.settings.SettingsPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun bindSettingsPreferences(settingsPreferences: SettingsPreferences): ISettingsPreferences
}