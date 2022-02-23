package com.example.forecast.feature_forecast.presentation

interface ChosenCityInterface {
    fun changeChosenInBase(newChosenID: String)

    fun getChosenCityID(): String
}