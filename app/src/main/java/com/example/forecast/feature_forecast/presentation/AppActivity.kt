package com.example.forecast.feature_forecast.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.forecast.R
import com.example.forecast.feature_forecast.presentation.fragments.MainPageFragment
import dagger.hilt.android.AndroidEntryPoint

const val P_LOG = "PARCELABLE_LOG"

@AndroidEntryPoint
class MainPageActivity : AppCompatActivity(R.layout.main_activity), NavigationHost,
    ChosenCityInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigateTo(MainPageFragment.create(), addToBackstack = false)
        }
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .replace(R.id.container_main, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    override fun changeChosenInBase(newChosenIndex: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.chosen_pref_key), newChosenIndex)
            apply()
        }
        Log.d("CHOSEN", "changeChosenInBase: $newChosenIndex")
        Toast.makeText(this, "New chosen city index: $newChosenIndex", Toast.LENGTH_SHORT).show()
    }

    override fun getChosenCityID(): String {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = resources.getString(R.string.default_chosen_id)
        val chosenIndex = sharedPref.getString(getString(R.string.chosen_pref_key), defaultValue)
        Log.d("CHOSEN", "getChosenCItyID: $chosenIndex")
        Toast.makeText(this, "Current chosen: $chosenIndex", Toast.LENGTH_SHORT).show()
        return chosenIndex!!
    }
}