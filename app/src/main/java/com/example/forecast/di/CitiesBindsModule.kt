package com.example.forecast.di

import androidx.lifecycle.ViewModel
import com.example.forecast.feature_forecast.presentation.viewmodels.CitiesViewModel
import com.example.forecast.feature_forecast.presentation.viewmodels.MainPageViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class CitiesBindsModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun bindCitiesViewModel(citiesViewModel: CitiesViewModel): ViewModel

    @ActivityRetainedScoped
    @Binds
    abstract fun bindMainPageViewModel(mainPageViewModel: MainPageViewModel): ViewModel
}