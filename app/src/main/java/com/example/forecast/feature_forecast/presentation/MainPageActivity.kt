package com.example.forecast.feature_forecast.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.forecast.R
import com.example.forecast.feature_forecast.presentation.fragments.MainPageFragment
import dagger.hilt.android.AndroidEntryPoint

const val P_LOG = "PARCELABLE_LOG"

@AndroidEntryPoint
class MainPageActivity : AppCompatActivity(R.layout.main_activity), NavigationHost {
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
}