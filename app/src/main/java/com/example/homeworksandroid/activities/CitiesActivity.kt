package com.example.homeworksandroid.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.homeworksandroid.R
import com.example.homeworksandroid.fragments.CitiesFragment

// TODO: 27.12.2021 make cities screen without activity
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