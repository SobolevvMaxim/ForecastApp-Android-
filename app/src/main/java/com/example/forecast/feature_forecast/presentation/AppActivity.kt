package com.example.forecast.feature_forecast.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.forecast.R
import com.example.forecast.feature_forecast.presentation.fragments.CitiesFragment
import com.example.forecast.feature_forecast.presentation.fragments.MainPageFragment
import dagger.hilt.android.AndroidEntryPoint

const val P_LOG = "PARCELABLE_LOG"

@AndroidEntryPoint
class AppActivity : AppCompatActivity(R.layout.main_activity), NavigationHost,
    ChosenCityInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigateToMainFragment()
        }
    }

    override fun changeChosenInBase(newChosenID: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.chosen_pref_key), newChosenID)
            apply()
        }
        Log.d("CHOSEN", "changeChosenInBase: $newChosenID")
    }

    override fun getChosenCityID(): String {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = resources.getString(R.string.default_chosen_id)
        val chosenIndex = sharedPref.getString(getString(R.string.chosen_pref_key), defaultValue)
        Log.d("CHOSEN", "getChosenCItyID: $chosenIndex")
        return chosenIndex ?: "0"
    }

    private fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            .setCustomAnimations(
//                R.anim.slide_in,
//                R.anim.fade_out,
//                R.anim.fade_in,
//                R.anim.slide_out
//            )
            .add(R.id.container_main, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    private fun navigateToMainFragment() {
        navigateTo(MainPageFragment.create(), addToBackstack = false)
    }

    override fun navigateToCitiesFragment() {
        navigateTo(CitiesFragment.create(), addToBackstack = true)
    }


}