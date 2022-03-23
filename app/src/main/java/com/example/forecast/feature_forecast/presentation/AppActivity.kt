package com.example.forecast.feature_forecast.presentation

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.forecast.R
import com.example.forecast.feature_forecast.presentation.utils.ChosenCityInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.main_activity),
    ChosenCityInterface {

    override fun changeChosenInBase(newChosenID: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.chosen_pref_key), newChosenID)
            apply()
        }
        Log.d(getString(R.string.main_log), "Changing chosen in base (AppActivity): $newChosenID")
    }

    override fun getChosenCityID(): String {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = resources.getString(R.string.default_chosen_id)
        val chosenIndex = sharedPref.getString(getString(R.string.chosen_pref_key), defaultValue)
        Log.d(getString(R.string.main_log), "Getting chosen from base (AppActivity): $chosenIndex")
        return chosenIndex ?: "0"
    }
}