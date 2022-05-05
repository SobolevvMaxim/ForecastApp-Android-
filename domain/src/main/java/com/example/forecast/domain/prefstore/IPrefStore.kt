package com.example.forecast.domain.prefstore

import kotlinx.coroutines.flow.Flow

interface IPrefStore {

    fun getChosen(): Flow<String>

    suspend fun changeChosen(newChosenID: String)
}