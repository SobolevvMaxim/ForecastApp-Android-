package com.example.forecast.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.forecast.R
import com.example.forecast.fragments.CitiesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesActivity : AppCompatActivity(R.layout.acitvity_choose_city) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_cities, CitiesFragment.create())
                .commit()
        }
    }
}