package com.example.forecast.feature_forecast.presentation.prefstore

interface IPrefStore {

    fun getChosen(): kotlinx.coroutines.flow.Flow<String>

    suspend fun changeChosen(newChosenID: String)
}