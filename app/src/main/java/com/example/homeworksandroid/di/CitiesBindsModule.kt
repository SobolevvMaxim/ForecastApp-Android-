package com.example.homeworksandroid.di

import androidx.lifecycle.ViewModel
import com.example.homeworksandroid.viewmodels.CitiesViewModel
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
}