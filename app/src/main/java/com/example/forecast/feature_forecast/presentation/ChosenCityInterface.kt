package com.example.forecast.feature_forecast.presentation

interface ChosenCityInterface {
    fun changeChosenInBase(newChosenIndex: String)

    fun getChosenCityID(): String
}